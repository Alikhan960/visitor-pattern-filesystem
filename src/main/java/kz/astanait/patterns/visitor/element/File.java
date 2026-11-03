package kz.astanait.patterns.visitor.element;

import kz.astanait.patterns.visitor.visitor.FileSystemVisitor;

/** Leaf element. Immutable: it is safe to share between visitors and threads. */
public final class File implements FileSystemElement {

    private final String name;
    private final long sizeInBytes;

    public File(String name, long sizeInBytes) {
        if (sizeInBytes < 0) {
            throw new IllegalArgumentException("File size must not be negative: " + sizeInBytes);
        }
        this.name = name;
        this.sizeInBytes = sizeInBytes;
    }

    public String getName() {
        return name;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    @Override
    public void accept(FileSystemVisitor visitor) {
        visitor.visitFile(this); // double dispatch: static type of "this" is File
    }
}
