package ocr.training.mnist;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class MnistDbFile extends RandomAccessFile{

    private int count;
    
    public MnistDbFile(String name, String mode) throws IOException,  FileNotFoundException {
        super(name, mode);
        
        if(getMagicNumber() != readInt()) {
            String msg = "This MNIST DB file "+name+" should start with the number "+getMagicNumber()+".";
            throw new RuntimeException(msg);
        }
        
        count = readInt();
    }
    
    protected abstract int getMagicNumber();
    
    public long getCurrentIndex() throws IOException {
        return (getFilePointer() - getHeaderSize()) / getEntryLength() + 1;
    }
    
    public void setCurrentIndex(long curr){
        try{
            if(curr < 0 || curr > count){
                String msg = curr+" is not in the range 0 to "+count;
                throw new RuntimeException(msg);
            }
            seek(getHeaderSize() + (curr - 1)* getEntryLength());
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }
    
    public int getHeaderSize(){
        // Two integers
        return 8;
    }
    
    public int getEntryLength(){
        // Number of bytes for each entry
        return 1;
    }
    
    public void next() throws IOException{
        if(getCurrentIndex() < count){
            skipBytes(getEntryLength());
        }
    }
    
    public void prev() throws IOException{
        if(getCurrentIndex() > 0){
            seek(getFilePointer() - getEntryLength());
        }
    }
    
    public int getCount(){
        return count;
    }
    
}
