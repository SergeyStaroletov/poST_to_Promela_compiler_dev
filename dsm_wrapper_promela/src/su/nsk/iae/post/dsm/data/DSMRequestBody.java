package su.nsk.iae.post.dsm.data;

import java.util.Scanner;

public class DSMRequestBody {
    private final String id;
    private final String root;
    private final String fileName;
    private final String ast;

    public DSMRequestBody(String string) {
    	final Scanner scanner = new Scanner(string);
        this.id = scanner.nextLine();
        this.root = scanner.nextLine();
        this.fileName = scanner.nextLine();
        final StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        this.ast = builder.toString();
        scanner.close();
    }

    public String getId() {
        return id;
    }

    public String getRoot() {
        return root;
    }

    public String getFileName() {
        return fileName;
    }

    public String getAST() {
        return ast;
    }
}
