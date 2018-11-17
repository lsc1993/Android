package com.ls.gogosport.main.mainpage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ls.gogosport.R;

import java.util.ArrayList;

/**
 * 个人中心页面,提供个人信息,用户配置等
 *
 * @author liushuang
 */
public class UserPageFragment extends Fragment {

    private static final String SUB_TITLE_VISIABLE_FLAG = "gone";

    private RecyclerView menuList;
    private MenuListAdapter adapter;

    public UserPageFragment() {
    }

    public static UserPageFragment newInstance() {
        return new UserPageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_user_page_fragment, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        menuList = root.findViewById(R.id.user_center_menu_list);
        adapter = new MenuListAdapter();
        menuList.setAdapter(adapter);
        menuList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setOnItemClickListener(itemClickListener);
        adapter.setMenuItems(parseMenu());
        adapter.notifyDataSetChanged();
    }

    private ArrayList<MenuItem> parseMenu() {
        ArrayList<MenuItem> items = new ArrayList<>();
        String[] menus = getResources().getStringArray(R.array.user_center_menu_items);
        if (menus.length > 0) {
            MenuItem item;
            String[] dataTmp;
            for (String data : menus) {
                item = new MenuItem();
                dataTmp = data.split(":");
                try {
                    item.id = Integer.parseInt(dataTmp[0]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    item.id = -1;
                }
                item.menuTitle = dataTmp[1];
                item.menuSubTitle = dataTmp[2];
                item.menuIconName = dataTmp[3];
                items.add(item);
            }
        }
        return items;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class MenuListAdapter extends RecyclerView.Adapter implements View.OnClickListener {

        private ArrayList<MenuItem> menuItems;

        private OnItemClickListener onItemClickListener;

        public MenuListAdapter() {
            menuItems = new ArrayList<>();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.user_center_menu_item, parent, false);
            MenuView menuView = new MenuView(view);
            view.setOnClickListener(this);
            return menuView;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MenuView) {
                MenuView menuView = (MenuView) holder;
                holder.itemView.setTag(position);
                MenuItem item = menuItems.get(position);
                menuView.menuIcon.setImageResource(getResources().getIdentifier(item.menuIconName, "drawable", getContext().getPackageName()));
                menuView.menuText.setText(item.menuTitle);
                if (SUB_TITLE_VISIABLE_FLAG.equals(item.menuSubTitle)) {
                    menuView.menuSubText.setVisibility(View.GONE);
                } else {
                    menuView.menuSubText.setVisibility(View.VISIBLE);
                    menuView.menuSubText.setText(item.menuSubTitle);
                }
            }
        }

        @Override
        public int getItemCount() {
            return menuItems.size();
        }

        public void setMenuItems(ArrayList<MenuItem> menuItems) {
            this.menuItems = menuItems;
        }

        public ArrayList<MenuItem> getMenuItems() {
            return menuItems;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    private class MenuView extends RecyclerView.ViewHolder {

        ImageView menuIcon;
        TextView menuText;
        TextView menuSubText;

        public MenuView(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View root) {
            menuIcon = root.findViewById(R.id.user_center_menu_img);
            menuText = root.findViewById(R.id.user_center_menu_title);
            menuSubText = root.findViewById(R.id.user_center_menu_sub_title);
        }
    }

    private class MenuItem {
        int id;
        String menuIconName;
        String menuTitle;
        String menuSubTitle;
    }

    interface OnItemClickListener {
        void onItemClick(View v, final int position);
    }

    OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            if (adapter.getMenuItems().get(position).id != -1) {
                Toast.makeText(getContext(), "onItemClick" + position, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
