package com.sellersync.jantz.sellersync;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends Fragment {

    public FeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText feedback = (EditText) view.findViewById(R.id.etfeedback);
        final RadioButton usefulD = (RadioButton) view.findViewById(R.id.usefulD);
        final RadioButton usefulM = (RadioButton) view.findViewById(R.id.usefulM);
        final RadioButton usefulN = (RadioButton) view.findViewById(R.id.usefulN);
        final RadioButton impD = (RadioButton) view.findViewById(R.id.impD);
        final RadioButton impM = (RadioButton) view.findViewById(R.id.impM);
        final RadioButton impN = (RadioButton) view.findViewById(R.id.impN);
        final RadioButton useD = (RadioButton) view.findViewById(R.id.useD);
        final RadioButton useM = (RadioButton) view.findViewById(R.id.useM);
        final RadioButton useN = (RadioButton) view.findViewById(R.id.useN);
        final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        final Button feedbackButton = (Button) view.findViewById(R.id.feedbackbutton);


        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedbackRecipient = getString(R.string.feedbackEmail);
                String subject = getString(R.string.seller_sync_feedback);
                String userFeed = getString(R.string.user_says) + feedback.getText().toString() +
                        getString(R.string.useful_D) + usefulD.isChecked() +
                        getString(R.string.useful_M) + usefulM.isChecked() +
                        getString(R.string.useful_N) + usefulN.isChecked() +
                        getString(R.string.imp_D) + impD.isChecked() +
                        getString(R.string.imp_M) + impM.isChecked() +
                        getString(R.string.imp_N) + impN.isChecked() +
                        getString(R.string.use_D) + useD.isChecked() +
                        getString(R.string.use_M) + useM.isChecked() +
                        getString(R.string.use_N) + useN.isChecked() +
                        getString(R.string.user_gave_star_num) + ratingBar.getRating() + getString(R.string.stars);

                SendMail sm = new SendMail(getContext(), feedbackRecipient, subject, userFeed);

                //Executing sendmail to send email
                sm.execute();
            }
        });
    }
}
