package co.kubo.indiesco.adaptadores;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.Tour;

/**
 * Created by estacion on 31/01/18.
 */

public class CustomPagerAdapter extends PagerAdapter {
    Tour mContext;
    LayoutInflater mLayoutInflater;

    Integer images[]    = {R.drawable.tour1, R.drawable.tour2, R.drawable.tour3};
    Integer fondo []    = {R.drawable.tour1_bg, R.drawable.tour2_bg, R.drawable.tour3_bg};
    Integer titulo[]    = {R.string.tour_titulo_1, R.string.tour_titulo_2, R.string.tour_titulo_3};
    Integer subtitulo[] = {R.string.tour_subtitulo_1, R.string.tour_subtitulo_2, R.string.tour_subtitulo_3};

    public CustomPagerAdapter(Tour context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return images.length;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_tour, container, false);

        RelativeLayout fondoTour    = (RelativeLayout) itemView.findViewById(R.id.fondoTour);
        ImageView imgFondoTour      = (ImageView) itemView.findViewById(R.id.imgFondoTour);
        TextView tvTituloTour       = (TextView) itemView.findViewById(R.id.tvTituloTour);
        TextView tvSubTituloTour    = (TextView) itemView.findViewById(R.id.tvSubTituloTour);
        Button btnTour              = (Button) itemView.findViewById(R.id.btnTour);

        fondoTour.setBackgroundResource(fondo[position]);
        imgFondoTour.setBackgroundResource(images[position]);
        tvTituloTour.setText(titulo[position]);
        tvSubTituloTour.setText(subtitulo[position]);

        btnTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.abrirLogin();
            }
        });
        container.addView(itemView);
        return itemView;
    }//public Object instantiateItem

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
