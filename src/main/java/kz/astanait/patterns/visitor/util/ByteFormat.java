package kz.astanait.patterns.visitor.util;

/** Formats byte counts for humans (e.g. 1536 -> "1.5 KB"). */
public final class ByteFormat {

    private ByteFormat() {
    }

    public static String format(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        double kilobytes = bytes / 1024.0;
        if (kilobytes < 1024) {
            return String.format("%.1f KB", kilobytes);
        }
        return String.format("%.1f MB", kilobytes / 1024.0);
    }
}
