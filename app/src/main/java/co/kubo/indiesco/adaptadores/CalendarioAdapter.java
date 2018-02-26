package co.kubo.indiesco.adaptadores;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.ViewHolderHeader;
import co.kubo.indiesco.activities.ViewHolderHeaderCalendario;
import co.kubo.indiesco.activities.ViewHolderListItem;
import co.kubo.indiesco.activities.ViewHolderListItemCalendario;
import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.utils.Utils;

/**
 * Created by Diego on 24/02/2018.
 */

public class CalendarioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final String TAG = "CalendarioAdapter";
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private ArrayList<Historial> calendar;
    private ArrayList<String> fecha;
    Activity activity;

    public CalendarioAdapter(ArrayList<Historial> calendar, Activity activity){
        this.calendar = calendar;
        this.activity=activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_calendario, parent, false);
            ViewHolderListItemCalendario holder = new ViewHolderListItemCalendario(v);
            return holder;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_calendario, parent, false);
            return new ViewHolderHeaderCalendario(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolderListItemCalendario) {
            ((ViewHolderListItemCalendario) holder).setTvDir(calendar.get(position).getDireccion());

            String [] splitHora_1 = calendar.get(position).getFecha_transaccion().split(" ");
            String [] splitHora_2 = splitHora_1[1].split(":");
            String hora = splitHora_2[0].concat(":").concat(splitHora_2[1]);
            ((ViewHolderListItemCalendario) holder).setTvHoraCalendar(hora);

            String url = "http://maps.google.com/maps/api/staticmap?center=" + calendar.get(position).getLatitud() + "," + calendar.get(position).getLongitud() + "&zoom=15&size=200x200&sensor=false";
            ((ViewHolderListItemCalendario) holder).setWebViewMap(url);

        } else if (holder instanceof ViewHolderHeaderCalendario) {
            Utils utils = new Utils();
            String nueva_fecha = utils.StringToDate(calendar.get(position).getFecha_transaccion());
            String [] extractMes = nueva_fecha.split(" ");
            String month = extractMes[1];
            ((ViewHolderHeaderCalendario) holder).setTvHeader(month);
        }
    }

    @Override
    public int getItemCount() {
        return calendar.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        if (calendar.get(position).getIsHeader().equals("no")){
            return false;
        }else{
            return true;
        }
    }

}