package com.aiyaapp.aiya.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aiyaapp.aiya.R;
import com.aiyaapp.aiya.camera.MenuBean;
import com.aiyaapp.camera.sdk.AiyaEffects;
import com.aiyaapp.camera.sdk.base.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by aiya on 2017/7/27.
 */

public class EffectAdapter extends RecyclerView.Adapter<ImageHolder> implements View.OnClickListener {

    public int[] effectIcons = new int[]{
            R.mipmap.no_eff, R.mipmap.img2017, R.mipmap.baowener, R.mipmap.gougou, R.mipmap.fadai, R.mipmap.grass, R.mipmap.huahuan, R.mipmap.majing, R.mipmap.maoer, R.mipmap.maorong, R.mipmap.meihualu, R.mipmap.niu, R.mipmap.shoutao, R.mipmap.tuer, R.mipmap.gaokongshiai, R.mipmap.shiwaitaoyuan, R.mipmap.mojing, R.mipmap.mogulin, R.mipmap.xiaohongmao
    };

    private OnEffectCheckListener listener;

    private ArrayList<MenuBean> mMenuDatas;
    private Context context;

    private int selectPos = 0;

    public EffectAdapter(Context context) {
        this.context = context;
        mMenuDatas = new ArrayList<>();
        initMenuData("modelsticker/stickers.json");
        for (int i = 0; i < effectIcons.length && i < mMenuDatas.size(); i++) {
            mMenuDatas.get(i).icon = effectIcons[i];
        }
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ImageHolder((LayoutInflater.from(context).inflate(R.layout.item_image, viewGroup, false)),this);
    }

    @Override
    public void onBindViewHolder(ImageHolder effectHolder, int i) {
        effectHolder.effect.setImageResource(mMenuDatas.get(i).icon);
        effectHolder.effect.setTag(i);
        effectHolder.effect.setSelected(selectPos == i && selectPos != 0);
    }

    //初始化特效按钮菜单
    private void initMenuData(String menuPath) {
        try {
            Log.e("解析菜单->" + menuPath);
            JsonReader r = new JsonReader(new InputStreamReader(context.getAssets().open(menuPath)));
            r.beginArray();
            while (r.hasNext()) {
                MenuBean menu = new MenuBean();
                r.beginObject();
                String name;
                while (r.hasNext()) {
                    name = r.nextName();
                    if (name.equals("name")) {
                        menu.name = r.nextString();
                    } else if (name.equals("path")) {
                        menu.path = r.nextString();
                    }
                }
                mMenuDatas.add(menu);
                Log.e("增加菜单->" + menu.name);
                r.endObject();
            }
            r.endArray();
            MenuBean bean = new MenuBean();
            bean.name = "本地";
            bean.path = "";
            bean.icon=R.mipmap.more;
            mMenuDatas.add(bean);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mMenuDatas.size();
    }

    public void setEffectCheckListener(OnEffectCheckListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null&&(int)v.getTag()==effectIcons.length&&listener.onEffectChecked(-1,"")){
            return;
        }
        selectPos = (int) v.getTag();
        if (selectPos == 0) {
            AiyaEffects.getInstance().setEffect(null);
        }else{
            AiyaEffects.getInstance().setEffect("assets/modelsticker/" + mMenuDatas.get(selectPos).path);
        }
        notifyDataSetChanged();
    }

    public interface OnEffectCheckListener{
        boolean onEffectChecked(int pos,String path);
    }

}
