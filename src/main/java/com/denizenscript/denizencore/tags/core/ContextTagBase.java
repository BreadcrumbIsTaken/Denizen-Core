package com.denizenscript.denizencore.tags.core;

import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.utilities.CoreConfiguration;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.Deprecations;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.tags.TagManager;

public class ContextTagBase {

    public ContextTagBase() {
        // Intentionally no docs
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                contextTags(event);
            }
        }, "context", "c");
        // Intentionally no docs
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                savedEntryTags(event);
            }
        }, "entry");
    }

    public void contextTags(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes();
        if (!event.matches("context", "c") || attribute.context.contextSource == null) {
            return;
        }
        if (event.matches("c")) {
            Deprecations.contextShorthand.warn(event.getScriptEntry());
        }
        String contextName = attribute.getAttributeWithoutParam(2);
        ObjectTag obj = event.getAttributes().context.contextSource.getContext(contextName);
        if (obj != null) {
            event.setReplacedObject(CoreUtilities.autoAttrib(obj, attribute.fulfill(2)));
            return;
        }
        if (!event.hasAlternative()) {
            attribute.echoError("Invalid context ID '" + contextName + "'!");
        }
    }

    public void savedEntryTags(ReplaceableTagEvent event) {
        if (!event.matches("entry")
                || event.getScriptEntry() == null) {
            return;
        }
        Attribute attribute = event.getAttributes();
        if (!attribute.hasParam()) {
            return;
        }
        if (event.getScriptEntry().getResidingQueue() != null) {
            String id = attribute.getParam();
            ScriptEntry held = event.getScriptEntry().getResidingQueue().getHeldScriptEntry(id);
            if (held == null) {
                if (!event.hasAlternative()) {
                    Debug.echoDebug(event.getScriptEntry(), "Bad saved entry ID '" + id + "'");
                }
            }
            else {
                String attrib = CoreUtilities.toLowerCase(attribute.getAttributeWithoutParam(2));
                ObjectTag got = held.getObjectTag(attrib);
                if (got == null) {
                    if (!event.hasAlternative()) {
                        Debug.echoDebug(event.getScriptEntry(), "Missing saved entry object '" + attrib + "'");
                        if (CoreConfiguration.debugVerbose) {
                            Debug.log("Option set is: " + held.getObjects().keySet());
                        }
                    }
                }
                else {
                    event.setReplacedObject(CoreUtilities.autoAttribTyped(got, attribute.fulfill(2)));
                }
            }
        }
    }
}
