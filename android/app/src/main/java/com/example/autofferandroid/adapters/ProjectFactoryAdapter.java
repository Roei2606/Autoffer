//package com.example.autofferandroid.adapters;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.autofferandroid.R;
//import com.example.projects_sdk.models.Project;
//import com.example.projects_sdk.models.QuoteStatus;
//import com.example.core_models_sdk.models.User;
//import com.example.users_sdk.network.SessionManager;
//import com.example.users_sdk.network.UserManager;
//
//import com.google.android.material.imageview.ShapeableImageView;
//
//import java.util.List;
//import java.util.Map;
//
//public class ProjectFactoryAdapter extends RecyclerView.Adapter<ProjectFactoryAdapter.ProjectViewHolder> {
//
//    public interface OnProjectActionListener {
//        void onViewBoq(Project project);
//        void onCreateQuote(Project project);
//        void onSendQuote(Project project);
//    }
//
//    private final List<Project> projectList;
//    private final Context context;
//    private final OnProjectActionListener actionListener;
//    private final UserManager userManager;
//
//    public ProjectFactoryAdapter(List<Project> projectList,
//                                 Context context,
//                                 OnProjectActionListener actionListener) {
//        this.projectList = projectList;
//        this.context = context;
//        this.actionListener = actionListener;
//        this.userManager = new UserManager();
//    }
//
//    @NonNull
//    @Override
//    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_project_factory, parent, false);
//        return new ProjectViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
//        Project project = projectList.get(position);
//        String currentFactoryId = SessionManager.getInstance().getCurrentUserId();
//
//        // כתובת
//        holder.projectAddress.setText(project.getProjectAddress());
//
//        // סטטוס למפעל הנוכחי
//        Map<String, QuoteStatus> statuses = project.getQuoteStatuses();
//        QuoteStatus myStatus = statuses != null ? statuses.get(currentFactoryId) : null;
//        holder.factoryStatus.setText("Status: " + (myStatus != null ? myStatus.name() : "UNKNOWN"));
//
//        // פרטי לקוח – נשלפים משרת
//        userManager.getUserById(project.getClientId()).thenAccept(client -> {
//            holder.clientName.setText("Client: " + client.getFirstName() + " " + client.getLastName());
//            holder.clientPhone.setText("Phone: " + client.getPhoneNumber());
//            holder.clientEmail.setText("Email: " + client.getEmail());
//        }).exceptionally(e -> {
//            holder.clientName.setText("Client: Error");
//            holder.clientPhone.setText("Phone: -");
//            holder.clientEmail.setText("Email: -");
//            return null;
//        });
//
//        // כפתור צפייה ב־BOQ
//        holder.buttonViewBoq.setOnClickListener(v -> actionListener.onViewBoq(project));
//
//        // כפתור יצירת הצעת מחיר
//        if (myStatus == QuoteStatus.ACCEPTED) {
//            holder.buttonCreateQuote.setVisibility(View.VISIBLE);
//            holder.buttonCreateQuote.setOnClickListener(v -> actionListener.onCreateQuote(project));
//        } else {
//            holder.buttonCreateQuote.setVisibility(View.GONE);
//        }
//
//        // כפתור שליחת הצעה
//        if (myStatus == QuoteStatus.ACCEPTED || myStatus == QuoteStatus.RECEIVED) {
//            holder.buttonSendQuote.setVisibility(View.VISIBLE);
//            holder.buttonSendQuote.setOnClickListener(v -> actionListener.onSendQuote(project));
//        } else {
//            holder.buttonSendQuote.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return projectList.size();
//    }
//
//    static class ProjectViewHolder extends RecyclerView.ViewHolder {
//
//        TextView projectAddress, clientName, clientPhone, clientEmail, factoryStatus;
//        ShapeableImageView buttonViewBoq, buttonCreateQuote, buttonSendQuote;
//
//        public ProjectViewHolder(@NonNull View itemView) {
//            super(itemView);
//            projectAddress = itemView.findViewById(R.id.textProjectAddress);
//            clientName = itemView.findViewById(R.id.textClientName);
//            clientPhone = itemView.findViewById(R.id.textClientPhone);
//            clientEmail = itemView.findViewById(R.id.textClientEmail);
//            factoryStatus = itemView.findViewById(R.id.textFactoryStatus);
//            buttonViewBoq = itemView.findViewById(R.id.buttonViewBoqPdf);
//            buttonCreateQuote = itemView.findViewById(R.id.buttonCreateQuote);
//            buttonSendQuote = itemView.findViewById(R.id.buttonSendQuote);
//        }
//    }
//}
