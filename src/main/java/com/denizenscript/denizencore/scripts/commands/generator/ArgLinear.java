package com.denizenscript.denizencore.scripts.commands.generator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Specifies that an argument is located by its linear order in the set of arguments not otherwise taken. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ArgLinear {
}
