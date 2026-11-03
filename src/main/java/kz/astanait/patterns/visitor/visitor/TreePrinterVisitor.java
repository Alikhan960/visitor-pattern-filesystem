package kz.astanait.patterns.visitor.visitor;

import kz.astanait.patterns.visitor.element.Directory;
import kz.astanait.patterns.visitor.element.File;
import kz.astanait.patterns.visitor.util.ByteFormat;

/**
 * Operation: render the structure as an indented tree.
 * Added later WITHOUT touching File or Directory (open/closed for new operations).
 */
public class TreePrinterVisitor implements FileSystemVisitor {

    private static final String INDENT = "  ";

    private final StringBuilder output = new StringBuilder();
    private int depth;

    @Override
    public void visitFile(File file) {
        appendLine("- " + file.getName() + " (" + ByteFormat.format(file.getSizeInBytes()) + ")");
    }

    @Override
    public void visitDirectory(Directory directory) {
        appendLine("+ " + directory.getName() + "/");
        depth++;
        directory.acceptChildren(this);
        depth--;
    }

    public String getTree() {
        return output.toString();
    }

    private void appendLine(String text) {
        output.append(INDENT.repeat(depth)).append(text).append(System.lineSeparator());
    }
}
