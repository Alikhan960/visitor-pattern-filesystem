package kz.astanait.patterns.visitor.visitor;

import kz.astanait.patterns.visitor.element.Directory;
import kz.astanait.patterns.visitor.element.File;

/** Operation: compute the total size of everything it visits. Use one instance per traversal. */
public class SizeCalculatorVisitor implements FileSystemVisitor {

    private long totalSizeInBytes;

    @Override
    public void visitFile(File file) {
        totalSizeInBytes += file.getSizeInBytes();
    }

    @Override
    public void visitDirectory(Directory directory) {
        directory.acceptChildren(this);
    }

    public long getTotalSizeInBytes() {
        return totalSizeInBytes;
    }
}
