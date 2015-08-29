package greenmoonsoftware.gopherwave.intellij;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.impl.event.DocumentEventImpl;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.util.Date;

public class GopherMockDocument implements Document {
    private StringBuilder text;
    private DocumentListener documentListener;

    public GopherMockDocument(String text) {
        this.text = new StringBuilder(text);
    }

    private LineOffsets findLineOffsets(int lineNumber) {
        String[] lines = text.toString().split("\n");
        int offset = 0;
        int lineNum = 0;
        for (String line : lines) {
            LineOffsets rtn = new LineOffsets(offset, offset + line.length());
            if (lineNum == lineNumber) {
                return rtn;
            }
            offset += line.length() + 1;
            lineNum++;
        }
        throw new RuntimeException("Unable to find line number offsets for line #" + lineNumber);
    }

    @NotNull
    @Override
    public String getText() {
        return text.toString();
    }

    @NotNull
    @Override
    public String getText(@NotNull TextRange textRange) {
        return textRange.substring(text.toString());
    }

    @NotNull
    @Override
    public CharSequence getCharsSequence() {
        return text;
    }

    @NotNull
    @Override
    public CharSequence getImmutableCharSequence() {
        return text;
    }

    @NotNull
    @Override
    public char[] getChars() {
        return text.toString().toCharArray();
    }

    @Override
    public int getTextLength() {
        return text.length();
    }

    @Override
    public int getLineCount() {
        return (int)text.chars().mapToObj(i -> (char)i).filter(c -> c == '\n').count();
    }

    @Override
    public int getLineNumber(int offset) {
        return (int) getText()
                .substring(0, offset)
                .chars()
                .mapToObj(i -> (char)i)
                .filter(c -> c == '\n')
                .count();
    }

    @Override
    public int getLineStartOffset(int line) {
        return findLineOffsets(line).start;
    }

    @Override
    public int getLineEndOffset(int line) {
        return findLineOffsets(line).end;
    }

    @Override
    public void insertString(int i, @NotNull CharSequence charSequence) {
        text.insert(i, charSequence);
        if (documentListener != null) {
            documentListener.documentChanged(new DocumentEventImpl(this, i, "", charSequence, new Date().getTime(), false));
        }
    }

    @Override
    public void deleteString(int i, int i1) {
        text.delete(i, i1);
    }

    @Override
    public void replaceString(int i, int i1, @NotNull CharSequence charSequence) {
        String replaced = text.toString().substring(i, i1);
        text.replace(i, i1, charSequence.toString());
        if (documentListener != null) {
            documentListener.documentChanged(new DocumentEventImpl(this, i, replaced, charSequence,  new Date().getTime(), false));
        }
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public long getModificationStamp() {
        return 0;
    }

    @Override
    public void fireReadOnlyModificationAttempt() {

    }

    @Override
    public void addDocumentListener(@NotNull DocumentListener documentListener) {
        this.documentListener = documentListener;
    }

    @Override
    public void addDocumentListener(@NotNull DocumentListener documentListener, @NotNull Disposable disposable) {
        this.documentListener = documentListener;
    }

    @Override
    public void removeDocumentListener(@NotNull DocumentListener documentListener) {

    }

    @NotNull
    @Override
    public RangeMarker createRangeMarker(int i, int i1) {
        return null;
    }

    @NotNull
    @Override
    public RangeMarker createRangeMarker(int i, int i1, boolean b) {
        return null;
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {

    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {

    }

    @Override
    public void setReadOnly(boolean b) {

    }

    @NotNull
    @Override
    public RangeMarker createGuardedBlock(int i, int i1) {
        return null;
    }

    @Override
    public void removeGuardedBlock(@NotNull RangeMarker rangeMarker) {

    }

    @Nullable
    @Override
    public RangeMarker getOffsetGuard(int i) {
        return null;
    }

    @Nullable
    @Override
    public RangeMarker getRangeGuard(int i, int i1) {
        return null;
    }

    @Override
    public void startGuardedBlockChecking() {

    }

    @Override
    public void stopGuardedBlockChecking() {

    }

    @Override
    public void setCyclicBufferSize(int i) {

    }

    @Override
    public void setText(@NotNull CharSequence charSequence) {

    }

    @NotNull
    @Override
    public RangeMarker createRangeMarker(@NotNull TextRange textRange) {
        return null;
    }

    @Override
    public int getLineSeparatorLength(int i) {
        return 0;
    }

    @Nullable
    @Override
    public <T> T getUserData(@NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T t) {

    }

    private class LineOffsets {
        public final int start;
        public final int end;

        private LineOffsets(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
}
