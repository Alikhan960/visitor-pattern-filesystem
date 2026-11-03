package kz.astanait.patterns.visitor;

import kz.astanait.patterns.visitor.element.Directory;
import kz.astanait.patterns.visitor.element.File;
import kz.astanait.patterns.visitor.visitor.FileCriteria;
import kz.astanait.patterns.visitor.visitor.SearchVisitor;
import kz.astanait.patterns.visitor.visitor.SizeCalculatorVisitor;
import kz.astanait.patterns.visitor.visitor.TreePrinterVisitor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/** Dependency-free tests. Run: java -cp out kz.astanait.patterns.visitor.VisitorSelfTest */
public class VisitorSelfTest {

    private static int checks;

    public static void main(String[] args) throws Exception {
        sizeIsSummedOverNestedDirectories();
        emptyDirectoryHasZeroSize();
        searchReturnsFullPaths();
        searchWithoutMatchesIsEmpty();
        resultListCannotBeModified();
        treePrinterIndentsByDepth();
        differentVisitorsCanRunConcurrentlyOnSameStructure();
        negativeSizeIsRejected();
        System.out.println("All " + checks + " checks passed.");
    }

    private static Directory sample() {
        return new Directory("root")
                .add(new File("a.java", 100))
                .add(new Directory("sub")
                        .add(new File("b.java", 200))
                        .add(new File("c.txt", 300)));
    }

    private static void sizeIsSummedOverNestedDirectories() {
        SizeCalculatorVisitor visitor = new SizeCalculatorVisitor();
        sample().accept(visitor);
        assertEquals(600L, visitor.getTotalSizeInBytes());
    }

    private static void emptyDirectoryHasZeroSize() {
        SizeCalculatorVisitor visitor = new SizeCalculatorVisitor();
        new Directory("empty").accept(visitor);
        assertEquals(0L, visitor.getTotalSizeInBytes());
    }

    private static void searchReturnsFullPaths() {
        SearchVisitor visitor = new SearchVisitor(FileCriteria.hasExtension(".java"));
        sample().accept(visitor);
        assertEquals(List.of("root/a.java", "root/sub/b.java"), visitor.getMatchingPaths());
    }

    private static void searchWithoutMatchesIsEmpty() {
        SearchVisitor visitor = new SearchVisitor(FileCriteria.largerThan(1_000));
        sample().accept(visitor);
        assertEquals(List.of(), visitor.getMatchingPaths());
    }

    private static void resultListCannotBeModified() {
        SearchVisitor visitor = new SearchVisitor(FileCriteria.nameContains("a"));
        sample().accept(visitor);
        try {
            visitor.getMatchingPaths().add("hack");
            throw new AssertionError("result list should be unmodifiable");
        } catch (UnsupportedOperationException expected) {
            checks++;
        }
    }

    private static void treePrinterIndentsByDepth() {
        TreePrinterVisitor visitor = new TreePrinterVisitor();
        sample().accept(visitor);
        String nl = System.lineSeparator();
        assertEquals("+ root/" + nl + "  - a.java (100 B)" + nl + "  + sub/" + nl
                + "    - b.java (200 B)" + nl + "    - c.txt (300 B)" + nl, visitor.getTree());
    }

    private static void differentVisitorsCanRunConcurrentlyOnSameStructure() throws Exception {
        Directory shared = sample();
        SizeCalculatorVisitor size = new SizeCalculatorVisitor();
        SearchVisitor search = new SearchVisitor(FileCriteria.hasExtension(".txt"));
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<?> first = pool.submit(() -> shared.accept(size));
            Future<?> second = pool.submit(() -> shared.accept(search));
            first.get();
            second.get();
        } finally {
            pool.shutdown();
        }
        assertEquals(600L, size.getTotalSizeInBytes());
        assertEquals(List.of("root/sub/c.txt"), search.getMatchingPaths());
    }

    private static void negativeSizeIsRejected() {
        try {
            new File("bad", -1);
            throw new AssertionError("negative size should be rejected");
        } catch (IllegalArgumentException expected) {
            checks++;
        }
    }

    private static void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("expected <" + expected + "> but was <" + actual + ">");
        }
        checks++;
    }
}
