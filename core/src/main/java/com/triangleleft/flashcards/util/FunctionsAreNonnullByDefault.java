package com.triangleleft.flashcards.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;

/**
 * This annotation can be applied to a package, class or method to indicate that all
 * method parameters and return values in that element are nonnull
 * by default unless overridden.
 */
@Documented
@Nonnull
@TypeQualifierDefault({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.CLASS)
public @interface FunctionsAreNonnullByDefault {
}
