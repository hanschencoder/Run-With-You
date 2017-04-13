package site.hanschen.runwithyou.ui.home.devicelist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import site.hanschen.runwithyou.R;
import site.hanschen.runwithyou.ui.home.devicelist.bean.Device;

/**
 * @author HansChen
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> implements View.OnClickListener {

    private List<Device>        mDevices;
    private Context             mContext;
    private LayoutInflater      mInflater;
    private OnItemClickListener mOnItemClickListener;

    public DeviceListAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<Device> devices) {
        this.mDevices = devices;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = mInflater.inflate(R.layout.list_item_two_line_with_icon, parent, false);
        root.setOnClickListener(DeviceListAdapter.this);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);

        Device device = mDevices.get(position);
        holder.deviceName.setText(device.getName());
        holder.deviceAddress.setText(device.getAddress());
    }

    @Override
    public int getItemCount() {
        return mDevices == null ? 0 : mDevices.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            Device device = mDevices.get((Integer) v.getTag());
            mOnItemClickListener.onItemClick(device);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(Device device);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView deviceName;
        TextView deviceAddress;

        ViewHolder(View itemView) {
            super(itemView);
            deviceName = (TextView) itemView.findViewById(R.id.primary_text);
            deviceAddress = (TextView) itemView.findViewById(R.id.secondary_text);
        }
    }
}
