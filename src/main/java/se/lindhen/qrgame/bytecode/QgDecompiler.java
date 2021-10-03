package se.lindhen.qrgame.bytecode;

import se.lindhen.qrgame.bytecode.versions.QgDecompilerV1;
import se.lindhen.qrgame.bytecode.versions.QgDecompilerV2;
import se.lindhen.qrgame.program.Program;

public class QgDecompiler {

    private final BitReader reader;

    public QgDecompiler(byte[] data) {
        this.reader = new BitReader(data);
    }

    public Program decompile() {
        int version = (int) reader.read(4);
        switch (version) {
            case 1: return new QgDecompilerV1(reader).decompile();
            case 2: return new QgDecompilerV2(reader).decompile();
            default: throw new RuntimeException("Cannot read version " + version);
        }
    }
}
