package paikannus.hackjunction.com.rfid_p;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.graphics.Color.WHITE;

public class bitMap extends Activity {

    View dView;
    Context cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cont = this;
        dView = new View(this);
        setContentView(dView);

    }

    @Override
    protected void onResume() {
        super.onResume();

        dView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        dView.pause();
    }

    class View extends SurfaceView implements Runnable {

        Thread thread = null;
        SurfaceHolder ourHolder;
        volatile boolean updating;
        Canvas canvas;
        Paint paint;
        Bitmap map;
        Bitmap ball;

        Bitmap ball1;
        Bitmap correct;
        int ballx = 100;
        int bally = 100;
        int ballx1 = 100;
        int bally1 = 100;
        int oldx = 100;
        int oldy = 100;
        float displayHeight=0;
        float displayWidth=0;
        Context cont;
        Boolean drawFound=false;
        Long time;
        OkHttpClient client;
        Boolean nextTimePause = false;
        Boolean check=true;

        public View(Context context) {
                    super(context);
                    cont = context;
            time = System.currentTimeMillis();
                    ourHolder = getHolder();
                    paint = new Paint();
                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                     displayHeight = displaymetrics.heightPixels / 2;
                    displayWidth= displaymetrics.widthPixels / 2;
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int height = new Double(displayMetrics.heightPixels*0.9).intValue();
                    int width = new Double(displayMetrics.widthPixels*0.9).intValue();
                    // Load Bob from his .png file
                    map = BitmapFactory.decodeResource(this.getResources(), R.drawable.kartta);
                    ball = BitmapFactory.decodeResource(this.getResources(), R.drawable.bal);
                    ball1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.bal1);
                    correct = BitmapFactory.decodeResource(this.getResources(), R.drawable.correct);


                    client = new OkHttpClient();

                    map = Bitmap.createScaledBitmap(map, width,
                            height, true);

                    ball = Bitmap.createScaledBitmap(ball, 70,
                            70, true);

                    ball1 = Bitmap.createScaledBitmap(ball1, 70,
                            70, true);

                    correct = Bitmap.createScaledBitmap(correct, 700,
                            200, true);

                    updating = true;

        }

        @Override
        public void run() {
            while (updating) {

                try {
                    update();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                draw();


            }

        }



        public void update() throws IOException {
        int plusx= 200;
        int plusy = 0;
            if (System.currentTimeMillis() - time > 3000) {

                String response = run("http://finnidhackjunctionrfidwebapi.azurewebsites.net/api/Epc/GetAll");
                int k = response.indexOf("x");
                System.out.println(response);
                time = System.currentTimeMillis();
                int l = response.indexOf("y");
                int newy=0;
                int newx = Integer.parseInt(response.substring(k+11, response.indexOf("."))) + plusx;
                try {
                    newy=Integer.parseInt(response.substring(l+11,l+16))+plusy;

                }catch (Exception e){
                    try{
                        newy=Integer.parseInt(response.substring(l+11,l+15))+plusy;
                    }catch (Exception t){

                        try {
                            newy=Integer.parseInt(response.substring(l+11,l+14))+plusy;
                        }   catch (Exception u){
                            try {
                                newy=Integer.parseInt(response.substring(l+11,l+13))+plusy;
                            }catch (Exception i){
                                try {
                                    newy=Integer.parseInt(response.substring(l+11,l+12))+plusy;
                                }catch (Exception p){
                                    Log.w("error", "homma kusi");
                                }
                            }

                        }
                    }
                }





                    int h = response.indexOf("confidence");
                try {
                    double confidence = Double.parseDouble(response.substring(h+12,h+16).replace(",",""));
                    if(confidence>2){
                        bally= newy;
                        ballx=newx;
                        System.out.println("conf" + confidence);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

        /*      if((Math.pow(Math.abs(oldx-newx),2)+Math.pow(Math.abs(oldy-newy),2)) < 10000){
                    ballx = newx;
                    bally = newy;
                }
                oldx = newx;
                oldy = newy;
                */





                for (int i = 0; i < response.length(); i++){
                    if(response.substring(0, i).endsWith("ylocation:")){
                        bally= Integer.parseInt(response.substring(i, 4));
                        break;
                    }
                }
                System.out.println("x="+ballx + "y="+bally);
                time = System.currentTimeMillis();


                if (calcDist(ballx, bally, ballx1, bally1)< 10 && calcDist(ballx, bally, ballx1, bally1)!=0){

                    drawFound= true;

                }

             /*   if(ballx<600){
                   ballx+=100;

               }else{ballx=0;
               }
                 if(bally<1000){
                   bally+=10;

               }else{
                     bally =0;
                 }*/
            }

        }

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }


        public void draw() {
            if(nextTimePause){pause();}
            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();

                canvas.drawColor(WHITE);
                canvas.drawBitmap(map, 40, 100, paint);

                paint.setColor(Color.RED);
                paint.setTextSize(50);
                canvas.drawText("You are here", ballx-80, bally - 10, paint);
                canvas.drawBitmap(ball, ballx, bally, paint);

                canvas.drawText("Petri", 220, 400-50, paint);

                canvas.drawBitmap(ball1, 220, 350, paint);
                if(drawFound){
                    canvas.drawBitmap(correct, 200, 600, paint);
                    paint.setColor(Color.BLACK);
                    canvas.drawText("Person found! ", 400, 700, paint);
                    paint.setTextSize(30);
                    canvas.drawText("Touch anywhere to return to main screen!", 300, 750, paint);
                    nextTimePause = true;

                }
                }
                try{
                ourHolder.unlockCanvasAndPost(canvas);}catch(Exception e){

                }




        }
        public double calcDist(int x1, int y1, int x2, int y2){
            int x = x1-x2;
            int y = y1-y2;
            return Math.sqrt(x^2+y^2);
        }


        public void pause() {
            updating = false;
            try {
                thread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }

        }

        public void resume() {
            updating = true;
            thread = new Thread(this);
            thread.start();
        }


        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if(drawFound){
                resume();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        updating = false;
                        Intent intent = new Intent (cont, mainActivity.class);
                        startActivity(intent);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    }
                });
                return true;
                }return true;






        }

    }

}