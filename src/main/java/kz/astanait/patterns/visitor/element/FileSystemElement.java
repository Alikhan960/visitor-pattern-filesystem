package kz.astanait.patterns.visitor.element;

import kz.astanait.patterns.visitor.visitor.FileSystemVisitor;

/**
 * Element interface of the Visitor pattern.
 * First half of double dispatch: the element decides WHICH visitX() is called.
 */
public interface FileSystemElement {

    void accept(FileSystemVisitor visitor);
}
