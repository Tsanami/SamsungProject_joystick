package com.example.samsungproject;



import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    Bitmap image, wall;
    Paint paint;
    float jumpCount = 10; // прыжок игрока
    boolean isJump = false; // падать или нет
    public static float iX, iY, tX = 0, tY = 0, wallX, wallY;
    float dx = 0, dy = 0;
    double k = 20; // velocity or koeff
    Resources res;
    MyThread myThread;
    //контроль столкновений и размеров
    float hi, wi;//ширина и высота изображения
    float hs, ws;//ширина и высота области рисования
    boolean isFirstDraw = true;
    GameMap gameMap;

    Rect wallRect, imageRect;

    public MySurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
        res = getResources();
        image = BitmapFactory.decodeResource(res, R.drawable.obito);
        wall = BitmapFactory.decodeResource(res, R.drawable.wall);
        hi = image.getHeight();
        wi = image.getWidth();
        iX = 100;
        iY = 100;

        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(5);
        setAlpha(0);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        myThread = new MyThread(getHolder(), this);
        myThread.setRunning(true);
        myThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        myThread.setRunning(false);
        try {
            myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(isFirstDraw){
            hs = getHeight();
            ws = getWidth();
            wallX = ws / 2;
            isJump = false;
            Random random = new Random();
            wallY = random.nextInt((int)(hs - wall.getHeight() - 5));
            wallRect = new Rect((int)wallX, (int)wallY, (int)(wallX + wall.getWidth()),
                    (int)(wallY + wall.getHeight()));
            gameMap = new GameMap((int)ws, (int)hs, res);

            iX = ws / 2;
            iY = hs-100; //4 * hs / 5 ;
            isFirstDraw = false;

        }

        gameMap.draw(canvas);
        canvas.drawBitmap(image, iX, iY, paint);
        //canvas.drawBitmap(wall, wallX, wallY, paint);
        //canvas.drawLine(iX, iY, tX, tY, paint);
        //if(tX != 0)
        //delta();
//        imageRect = new Rect((int)iX, (int)iY, (int) (iX + wi), (int)(iY + hi));
//
//        if(imageRect.intersect(wallRect)){
//            dy = 0;
//            dx = 0;
//        }

        iX += dx;
        iY += dy;



        checkScreen();
    }

    private void checkScreen(){

        // Отскок от экрана
//        if(iY + hi >= hs || iY <= 0)
//            dy = -dy;
//        if(iX + wi >= ws || iX <= 0)
//            dx = -dx;


        // Падение

        if ((jumpCount >= -10) && (isJump)){
            iY -= jumpCount * Math.abs(jumpCount) *0.5;
            jumpCount -= 1;
            delta();
        }

        else {
            jumpCount = 10;
            isJump = false;
            dx = 0;
        }



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            tX = event.getX();
            tY = event.getY();
            isJump = true;
        }
        return true;
    }
    //расчет смещения картинки по x и y
    void delta(){
        double ro = Math.sqrt((tX- iX)*(tX- iX)+(tY-iY)*(tY-iY));
        dx = (float) (k * (tX - iX)/ro);
        //dy = (float) (k * (tY - iY)/ro);
    }


}

//TODO Запретить в манифесте поворот
    //при создании ScreenOrientation LandScape
    //Метод в активности для фиксирования
    //SetRequestOrientation(LANDSCAPE)
    //конструктор включающий атрибуты