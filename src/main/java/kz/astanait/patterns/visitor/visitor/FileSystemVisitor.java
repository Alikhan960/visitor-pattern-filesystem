package kz.astanait.patterns.visitor.visitor;

import kz.astanait.patterns.visitor.element.Directory;
import kz.astanait.patterns.visitor.element.File;

/**
 * Visitor interface: one visitX() method per concrete element type.
 * Second half of double dispatch: the visitor decides WHAT happens for that type.
 */
public interface FileSystemVisitor {

    void visitFile(File file);

    void visitDirectory(Directory directory);
}
