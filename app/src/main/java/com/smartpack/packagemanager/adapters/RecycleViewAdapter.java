/*
 * Copyright (C) 2020-2021 sunilpaulmathew <sunil.kde@gmail.com>
 *
 * This file is part of Package Manager, a simple, yet powerful application
 * to manage other application installed on an android device.
 *
 */

package com.smartpack.packagemanager.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textview.MaterialTextView;
import com.smartpack.packagemanager.R;
import com.smartpack.packagemanager.activities.PackageDetailsActivity;
import com.smartpack.packagemanager.utils.PackageTasks;
import com.smartpack.packagemanager.utils.Utils;

import java.util.List;

/*
 * Created by sunilpaulmathew <sunil.kde@gmail.com> on October 08, 2020
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private List<String> data;

    public RecycleViewAdapter (List<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.ViewHolder holder, int position) {
        if (!Utils.isPackageInstalled(this.data.get(position), holder.appID.getContext())) {
            return;
        }
        holder.appIcon.setImageDrawable(PackageTasks.getAppIcon(this.data.get(position), holder.appIcon.getContext()));
        holder.appID.setText(this.data.get(position));
        if (Utils.mSearchText != null && PackageTasks.getAppName(this.data.get(position), holder.appName.getContext()).toLowerCase().contains(Utils.mSearchText)) {
            holder.appName.setText(Utils.fromHtml(PackageTasks.getAppName(this.data.get(position), holder.appName.getContext()).toLowerCase().replace(Utils.mSearchText,
                    "<b><i><font color=\"" + Color.RED + "\">" + Utils.mSearchText + "</font></i></b>")));
        } else {
            holder.appName.setText(PackageTasks.getAppName(this.data.get(position), holder.appName.getContext()));
        }
        holder.checkBox.setChecked(PackageTasks.mBatchList.contains(this.data.get(position)));
        holder.checkBox.setOnClickListener(v -> {
            if (PackageTasks.mBatchList.contains(this.data.get(position))) {
                PackageTasks.mBatchList.remove(this.data.get(position));
            } else {
                PackageTasks.mBatchList.add(this.data.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AppCompatImageButton appIcon;
        public MaterialCheckBox checkBox;
        private MaterialTextView appName;
        private MaterialTextView appID;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.appIcon = view.findViewById(R.id.icon);
            this.appName = view.findViewById(R.id.title);
            this.appID = view.findViewById(R.id.description);
            this.checkBox = view.findViewById(R.id.checkbox);
        }

        @Override
        public void onClick(View view) {
            if (!Utils.isPackageInstalled(this.appID.getText().toString(), view.getContext())) {
                Utils.snackbar(view, view.getContext().getString(R.string.package_removed));
                return;
            }
            Utils.mApplicationID = this.appID.getText().toString();
            Utils.mApplicationName = PackageTasks.getAppName(Utils.mApplicationID, view.getContext());
            Utils.mApplicationIcon = PackageTasks.getAppIcon(Utils.mApplicationID, view.getContext());
            Utils.mDirSource = PackageTasks.getSourceDir(Utils.mApplicationID, view.getContext());
            Utils.mDirData = PackageTasks.getDataDir(Utils.mApplicationID, view.getContext());
            Utils.mDirNatLib = PackageTasks.getNativeLibDir(Utils.mApplicationID, view.getContext());
            Utils.mSystemApp = PackageTasks.isSystemApp(Utils.mApplicationID, view.getContext());
            Intent details = new Intent(view.getContext(), PackageDetailsActivity.class);
            view.getContext().startActivity(details);
        }
    }

}