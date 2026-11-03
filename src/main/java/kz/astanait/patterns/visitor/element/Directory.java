package kz.astanait.patterns.visitor.element;

import kz.astanait.patterns.visitor.visitor.FileSystemVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite element. The child list is private; visitors reach the children only
 * through {@link #acceptChildren(FileSystemVisitor)}, so no mutable collection leaks out.
 * Build the structure completely before sharing it with visitors/threads.
 */
public final class Directory implements FileSystemElement {

    private final String name;
    private final List<FileSystemElement> children = new ArrayList<>();

    public Directory(String name) {
        this.name = name;
    }

    public Directory add(FileSystemElement child) {
        children.add(child);
        return this;
    }

    public String getName() {
        return name;
    }

    /** Lets a visitor continue its traversal without exposing the child list. */
    public void acceptChildren(FileSystemVisitor visitor) {
        for (FileSystemElement child : children) {
            child.accept(visitor);
        }
    }

    @Override
    public void accept(FileSystemVisitor visitor) {
        visitor.visitDirectory(this); // double dispatch: static type of "this" is Directory
    }
}
