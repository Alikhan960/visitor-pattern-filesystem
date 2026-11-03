package kz.astanait.patterns.visitor.visitor;

import kz.astanait.patterns.visitor.element.File;

import java.util.function.Predicate;

/** Ready-made search criteria for {@link SearchVisitor}. */
public final class FileCriteria {

    private FileCriteria() {
    }

    public static Predicate<File> hasExtension(String extension) {
        return file -> file.getName().endsWith(extension);
    }

    public static Predicate<File> largerThan(long sizeInBytes) {
        return file -> file.getSizeInBytes() > sizeInBytes;
    }

    public static Predicate<File> nameContains(String fragment) {
        return file -> file.getName().contains(fragment);
    }
}
