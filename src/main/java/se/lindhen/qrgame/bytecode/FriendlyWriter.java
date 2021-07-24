package se.lindhen.qrgame.bytecode;

public class FriendlyWriter extends BitWriter {

    final StringBuffer stringBuffer = new StringBuffer();

    @Override
    public void writeCommand(CommandCode command) {
        stringBuffer.append("\n");
        stringBuffer.append(command.toString());
    }

    @Override
    public void write(int size, long value) {
        stringBuffer.append("(");
        stringBuffer.append(size);
        stringBuffer.append(":");
        stringBuffer.append(value);
        stringBuffer.append(")");
    }

    @Override
    public void writeInt(int number) {
        stringBuffer.append("i");
        stringBuffer.append(number);
    }

    @Override
    public void writeBool(boolean bool) {
        stringBuffer.append('b');
        stringBuffer.append(bool);
    }

    @Override
    public void writePositiveByte(int value) {
        stringBuffer.append('p');
        stringBuffer.append(value);
    }

    public String toString() {
        return stringBuffer.toString();
    }
}
