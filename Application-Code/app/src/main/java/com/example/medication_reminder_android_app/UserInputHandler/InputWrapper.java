package com.example.medication_reminder_android_app.UserInputHandler;

import android.util.Log;

import com.example.medication_reminder_android_app.NotificationRelay.OutOfAppNotifications;
import com.example.medication_reminder_android_app.SQLiteDB.MainViewModel;
import com.example.medication_reminder_android_app.SQLiteDB.MedicationEntity;

import java.security.InvalidParameterException;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Used to contain the input handlers and send data to them to process.
 *
 * @author Samuel Jones
 * @since 3-1-2021
 */
public class InputWrapper {

    OutOfAppNotifications outOfAppNotifications;
    MedicationInputHandler medicationInputHandler;
    DoctorInputHandler doctorInputHandler;
    AppointmentInputHandler appointmentInputHandler;
    MainViewModel mvm;

    public enum InputType{Medication, Doctor, Appointment}


    /**
     * Makes a new InputWrapper
     */
    public InputWrapper(MainViewModel mainViewModel) {
        mvm = mainViewModel;
        medicationInputHandler = new MedicationInputHandler(mainViewModel);
        doctorInputHandler = new DoctorInputHandler();
        appointmentInputHandler = new AppointmentInputHandler();
    }

    public void setOutOfAppNotifications(OutOfAppNotifications a){
        this.outOfAppNotifications = a;
    }


    /**
     * Takes in an input map and sends it to the corresponding input handler
     * @param type The type of input
     * @param input The input map
     * @throws InvalidParameterException If type is invalid or input map is null
     * @return The reminder ID of the reminder entity associated with the given input
     */
    public void processInput(InputType type, Map<String,String> input) throws InvalidParameterException {
        if(input == null) {
            throw new InvalidParameterException("Invalid input map");
        }
        switch (type) {
            case Medication:
                medicationInputHandler.inputRequest(input);
                String medName = input.get("name");
                mvm.getMedByName(medName).subscribeOn(Schedulers.io()).subscribe(new DisposableSingleObserver<MedicationEntity>() {
                    @Override
                    public void onSuccess(@NonNull MedicationEntity medicationEntity) {
                        outOfAppNotifications.scheduleNotification(medicationEntity.getReminderID());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("app-debug", "Did not schedule Notif ", e);
                    }
                });
            case Doctor:
                //doctorInputHandler.inputRequest(input); TODO
                break;
            case Appointment:
                //appointmentInputHandler.inputRequest(input); TODO
                break;
            default:
                throw new InvalidParameterException("Invalid input type");
        }
    }


    /**
     * Requests the deletion of an entry from the internal database with the given type and name
     * @param type The type of entry
     * @param name The name of the entry
     * @throws InvalidParameterException If type is invalid or name string is null
     */
    public void processDeleteRequest(InputType type, String name) throws InvalidParameterException {
        if(name == null) {
            throw new InvalidParameterException("Invalid name String");
        }
        switch (type) {
            case Medication:
                medicationInputHandler.deleteRequest(name);
                break;
            case Doctor:
                //doctorInputHandler.inputRequest(input); TODO
                break;
            case Appointment:
                //appointmentInputHandler.inputRequest(input); TODO
                break;
            default:
                throw new InvalidParameterException("Invalid type");
        }
    }


    /**
     * Processes a request to acknowledge a notification
     * @param type The type of the reminder
     * @param reminderID The corresponding ID for the reminder to be acknowledged
     * @param dismissed Indicated whether the notification was dismissed or acknowledged; true for
     *                  dismissed, false for acknowledged
     * @return A date & time string of the format "YYYY-MM-DD HH:MM" of the next date and time for
     *         the reminder being acknowledged
     */
    public void processAcknowledgementRequest(InputType type, long reminderID, boolean dismissed) {
        switch (type) {
            case Medication:
                medicationInputHandler.acknowledgeNotificationRequest(reminderID, dismissed);
                outOfAppNotifications.scheduleNotification(reminderID);
                break;
            case Appointment:
                //return appointmentInputHandler.inputRequest(input); TODO
                break;
            default:
                throw new InvalidParameterException("Invalid type");
        }
    }


    /**
     * Requests to clear internal database tables of a specific type
     * @param type The type of the information to be cleared
     */
    public void clearTableRequest(InputType type) {
        switch (type) {
            case Medication:
                medicationInputHandler.deleteAllRequest();
                break;
            case Doctor:
                //doctorInputHandler.inputRequest(input); TODO
                break;
            case Appointment:
                //appointmentInputHandler.inputRequest(input); TODO
                break;
            default:
                throw new InvalidParameterException("Invalid type");
        }
    }

    /**
     * Requests to delete everything in the internal database
     */
    public void clearTableRequest() {
        medicationInputHandler.deleteAllRequest();
        //doctorInputHandler.deleteAllRequest();
        //appointmentInputHandler.deleteAllRequest();
    }

}
