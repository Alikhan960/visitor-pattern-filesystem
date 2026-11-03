package kz.astanait.patterns.visitor;

import kz.astanait.patterns.visitor.element.Directory;
import kz.astanait.patterns.visitor.element.File;
import kz.astanait.patterns.visitor.util.ByteFormat;
import kz.astanait.patterns.visitor.visitor.FileCriteria;
import kz.astanait.patterns.visitor.visitor.SearchVisitor;
import kz.astanait.patterns.visitor.visitor.SizeCalculatorVisitor;
import kz.astanait.patterns.visitor.visitor.TreePrinterVisitor;

/** Client: builds one object structure and runs different visitors over it. */
public class Main {

    public static void main(String[] args) {
        Directory project = buildSampleProject();

        System.out.println("=== Object structure (TreePrinterVisitor, added later) ===");
        TreePrinterVisitor treePrinter = new TreePrinterVisitor();
        project.accept(treePrinter);
        System.out.print(treePrinter.getTree());

        System.out.println();
        System.out.println("=== SizeCalculatorVisitor ===");
        SizeCalculatorVisitor sizeCalculator = new SizeCalculatorVisitor();
        project.accept(sizeCalculator);
        System.out.println("Total size of 'project': "
                + ByteFormat.format(sizeCalculator.getTotalSizeInBytes())
                + " (" + sizeCalculator.getTotalSizeInBytes() + " bytes)");

        System.out.println();
        System.out.println("=== SearchVisitor: files with extension .java ===");
        SearchVisitor javaFiles = new SearchVisitor(FileCriteria.hasExtension(".java"));
        project.accept(javaFiles);
        javaFiles.getMatchingPaths().forEach(path -> System.out.println("  " + path));

        System.out.println();
        System.out.println("=== SearchVisitor: files larger than 10 KB ===");
        SearchVisitor bigFiles = new SearchVisitor(FileCriteria.largerThan(10 * 1024));
        project.accept(bigFiles);
        bigFiles.getMatchingPaths().forEach(path -> System.out.println("  " + path));
    }

    private static Directory buildSampleProject() {
        Directory model = new Directory("model")
                .add(new File("User.java", 900))
                .add(new File("Order.java", 1_500));
        Directory source = new Directory("src")
                .add(new File("Main.java", 4_200))
                .add(new File("Utils.java", 1_800))
                .add(model);
        Directory docs = new Directory("docs")
                .add(new File("design.pdf", 250_000))
                .add(new File("notes.txt", 700));
        Directory assets = new Directory("assets")
                .add(new File("logo.png", 48_000))
                .add(new File("banner.png", 120_000));
        return new Directory("project")
                .add(new File("README.md", 2_048))
                .add(source)
                .add(docs)
                .add(assets);
    }
}
