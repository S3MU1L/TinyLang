package com.tinylang.util;

import java.nio.file.Path;

public class TLangFileValidator {
    
    private static final String TLANG_SUFFIX = ".tl";

    public static boolean isValidTLangFile(Path path)  {
        return (path.getFileName().toString().endsWith(TLANG_SUFFIX));
    }
}
