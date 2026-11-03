package kz.astanait.patterns.visitor.visitor;

import kz.astanait.patterns.visitor.element.Directory;
import kz.astanait.patterns.visitor.element.File;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Predicate;

/** Operation: collect the full paths of all files matching a criterion. One instance per traversal. */
public class SearchVisitor implements FileSystemVisitor {

    private final Predicate<File> criterion;
    private final Deque<String> currentPath = new ArrayDeque<>();
    private final List<String> matchingPaths = new ArrayList<>();

    public SearchVisitor(Predicate<File> criterion) {
        this.criterion = criterion;
    }

    @Override
    public void visitFile(File file) {
        if (criterion.test(file)) {
            matchingPaths.add(pathOf(file));
        }
    }

    @Override
    public void visitDirectory(Directory directory) {
        currentPath.addLast(directory.getName());
        directory.acceptChildren(this);
        currentPath.removeLast();
    }

    /** Returns a copy, so callers cannot modify the visitor's internal state. */
    public List<String> getMatchingPaths() {
        return List.copyOf(matchingPaths);
    }

    private String pathOf(File file) {
        return String.join("/", currentPath) + "/" + file.getName();
    }
}
