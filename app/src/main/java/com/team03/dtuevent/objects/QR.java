package com.team03.dtuevent.objects;

import android.graphics.Bitmap;

import com.team03.dtuevent.callbacks.Observer;
import com.team03.dtuevent.objects.data.created.ICreatedData;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;


public class QR {
    private ICreatedData contents;
    private int color;
    private int background;
    private final ArrayList<Observer<QR>> observers;



    public QR(ICreatedData contents, int color, int background) {
        this.contents = contents;
        this.color = color;
        this.background = background;
        observers = new ArrayList<>();
    }

    public void registerObserver(Observer<QR> observer) {
        observers.add(observer);
    }


    public ICreatedData getContents() {
        return contents;
    }

    public int getColor() {
        return color;
    }

    public int getBackground() {
        return background;
    }


    public void setContents(ICreatedData contents) {
        this.contents = contents;
        notifyObservers();
    }

    public void setColor(int color) {
        this.color = color;
        notifyObservers();
    }

    public void setBackground(int background) {
        this.background = background;
        notifyObservers();
    }

    public Bitmap generateBitmap() throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        if (contents == null || contents.isEmpty()) {
            return null;
        }


        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix =
                writer.encode(contents.getQRData(), BarcodeFormat.QR_CODE,
                        400, 400, hints);

        int height = bitMatrix.getHeight();
        int width = bitMatrix.getWidth();


        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? color : background);
            }
        }
        return bmp;
    }

    public void writeBitmapToOutput(OutputStream os) throws WriterException{
        generateBitmap()
                .compress(Bitmap.CompressFormat.PNG, 90, os);
    }


    private void notifyObservers() {
        for (Observer<QR> o : observers) {
            o.valueChanged(this);
        }
    }


}
