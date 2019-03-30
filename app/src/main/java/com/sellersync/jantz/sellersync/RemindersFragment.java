package com.sellersync.jantz.sellersync;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class RemindersFragment extends Fragment implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    ListView reminderList;
    EditText addEntry;
    Button btnAdd, update, clear, delete;
    ArrayList<String> reminders = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    DbHelper dbHelper;
    Calendar c;
    Integer year,month,day,hour,minute,yearFinal,monthFinal,dayFinal,hourFinal,minuteFinal;
    AlarmManager alarm_manager;
    PendingIntent mAlarmIntent;
    TextView tvResult;
    String checkedList;

    public RemindersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminders, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.tvPageTitle.setText(R.string.reminders);

        reminderList = (ListView) view.findViewById(R.id.listReminder);
        addEntry = (EditText) view.findViewById(R.id.etListItem);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        update = (Button) view.findViewById(R.id.btnUpdate);
        clear = (Button) view.findViewById(R.id.btnClear);
        delete = (Button) view.findViewById(R.id.btnDelete);
        dbHelper = new DbHelper(getContext());

        // SET ADAPTER
        loadReminderList();


        // SET SELECTED ITEM
        reminderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

                checkedList = (reminders.get(pos));

            }
        });

        //HANDLE BUTTON EVENTS

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), RemindersFragment.this,
                        year, month, day);
                datePickerDialog.show();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!checkedList.isEmpty() && checkedList.length() > 0) {
                        delete();
                        Calendar c = Calendar.getInstance();
                        year = c.get(Calendar.YEAR);
                        month = c.get(Calendar.MONTH);
                        day = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), RemindersFragment.this,
                                year, month, day);
                        datePickerDialog.show();
                    } else {

                    }
                } catch (Exception e) {}

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

    }


    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        yearFinal = i;
        monthFinal = i1 + 1;
        dayFinal = i2;

        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), RemindersFragment.this,
                hour, minute, DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.show();

    }

    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hourFinal = i;
        minuteFinal = i1;

        startAlarm(getView());
    }


    public void startAlarm(View view) {

        final EditText reminderEditText = new EditText(getContext());
        AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle(R.string.add_new_reminder)
                .setMessage(R.string.remind_me_to)
                .setView(reminderEditText)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String reminder = String.valueOf(reminderEditText.getText());
                        if (reminder.length() > 0 || !reminder.equals("")) {

                            if (!dbHelper.getReminderList().contains(reminder)) {

                                int setAlarmId = getAlarmId(getContext());

                                alarm_manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                                Intent intent = new Intent(getContext(), AlarmReceiver.class);
                                intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                                mAlarmIntent = PendingIntent.getBroadcast(getActivity(), setAlarmId, intent, 0);

                                Calendar calNow = Calendar.getInstance();
                                Calendar calSet = (Calendar) calNow.clone();

                                calSet.set(Calendar.DAY_OF_MONTH, dayFinal);
                                calSet.set(Calendar.HOUR_OF_DAY, hourFinal);
                                calSet.set(Calendar.MINUTE, minuteFinal);
                                calSet.set(Calendar.SECOND, 0);
                                calSet.set(Calendar.MILLISECOND, 0);

                                Long time = calSet.getTimeInMillis();
                                if(time < System.currentTimeMillis()) {
                                    Calendar c = Calendar.getInstance();
                                    year = c.get(Calendar.YEAR);
                                    month = c.get(Calendar.MONTH);
                                    day = c.get(Calendar.DAY_OF_MONTH);

                                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), RemindersFragment.this,
                                            year, month, day);
                                    datePickerDialog.show();
                                    Toast.makeText(getContext(), R.string.please_set_to_future,
                                            Toast.LENGTH_LONG).show();
                                } else {

                                    if (!dbHelper.getReminderIdList().contains(time)) {
                                        add(reminder, time, setAlarmId);

                                        alarm_manager.set(AlarmManager.RTC_WAKEUP, time, mAlarmIntent);


                                        //tvResult.setText("year: " + yearFinal + "\n" +                                                       // FIXME: 6/17/2017
                                        // "month: " + monthFinal + "\n" +
                                        // "day: " + dayFinal + "\n" +
                                        //"hour: " + hourFinal + "\n" +
                                        //"minute: " + minuteFinal);
                                    } else {
                                        Calendar c = Calendar.getInstance();
                                        year = c.get(Calendar.YEAR);
                                        month = c.get(Calendar.MONTH);
                                        day = c.get(Calendar.DAY_OF_MONTH);

                                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), RemindersFragment.this,
                                                year, month, day);
                                        datePickerDialog.show();
                                        Toast.makeText(getContext(), R.string.please_choose_different_time,
                                                Toast.LENGTH_LONG).show();
                                    }
                                }

                            } else {
                                reminderList.setItemChecked(adapter.getPosition(reminder), false);
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage(reminder + getString(R.string.please_choose_different_reminder_name))
                                        .setNegativeButton(R.string.ok, null)
                                        .create()
                                        .show();
                            }
                        } else {
                            startAlarm(getView());
                            Toast.makeText(getContext(), R.string.please_enter_reminder_name,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.show();
    }

    public static int getAlarmId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int alarmId = preferences.getInt("ALARM", 1);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("ALARM", alarmId + 1).apply();
        return alarmId;
    }

    private void loadReminderList() {
        if(adapter==null) {
            adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_single_choice, reminders);
            if(dbHelper.getReminderList().size() == 0) {
                Toast.makeText(getContext(), R.string.no_reminders,
                        Toast.LENGTH_LONG).show();
            }
            reminderList.setAdapter(adapter);
            adapter.addAll(dbHelper.getReminderList());
        }
        else {

            adapter.notifyDataSetChanged();
        }
    }

    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    // ADD
    private void add (String reminder, Long timeId, Integer intentId) {

        if(!reminder.isEmpty() && reminder.length() > 0) {

                //ADD TO DATABASE
            String reminderFull = "\t"+ reminder + "\n" + convertDate(String.valueOf(timeId), "dd/MM/yyyy hh:mm aa");
                dbHelper.insertNewReminder(reminderFull, timeId, intentId);

                loadReminderList();

                //ADD

                adapter.add("\t"+ reminder + "\n" + convertDate(String.valueOf(timeId), "dd/MM/yyyy hh:mm aa"));

                //REFRESH
                adapter.notifyDataSetChanged();

                Toast.makeText(getContext(),  reminder +getString(R.string.added_to_reminders), Toast.LENGTH_SHORT).show();
            reminderList.setItemChecked(0, false);

        } else {

        }
    }

    public void cancelAlarm(View view) {

        //Log.i(TAG, "cancelAlarm");

        // If the alarm has been set, cancel it.
        if (alarm_manager!= null) {
            alarm_manager.cancel(mAlarmIntent);
        }
    }

    //DELETE
    private void delete() {
        int pos = reminderList.getCheckedItemPosition();
        if(pos > -1) {
            if (!checkedList.isEmpty() && checkedList.length() > 0) {
                //REMOVE
                try {
                    int alarmId = dbHelper.getIntentId(checkedList);
                    Long alarmTime = dbHelper.getTime(checkedList);
                    alarm_manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                    mAlarmIntent = PendingIntent.getBroadcast(getActivity(), alarmId, intent, 0);
                    alarm_manager.set(AlarmManager.RTC_WAKEUP, alarmTime, mAlarmIntent);
                    alarm_manager.cancel(mAlarmIntent);


                    dbHelper.deleteReminder(checkedList);
                } catch (Exception e) {
                }


                adapter.remove(checkedList);
                //REFRESH
                checkedList = "";
                adapter.notifyDataSetChanged();

                if (checkedList != "") {
                    Toast.makeText(getContext(), checkedList + getString(R.string.deleted_from_reminders), Toast.LENGTH_SHORT).show();
                }
                reminderList.setItemChecked(0, false);
            } else {

            }
        } else {

        }
    }
    //CLEAR
    private void clear () {
        dbHelper.deleteAllReminder();
        adapter.clear();
        checkedList = "";
        cancelAlarm(getView());
    }


}


