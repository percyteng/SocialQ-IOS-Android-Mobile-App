package hackers.orbit.orbit;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by percy on 2016/3/5.
 */

public class allBoards extends Fragment implements  View.OnClickListener {
    private View myFragmentView;
    final int QUEENS = 0;
    final int UWO = 1;
    final int UOT = 2;
    final int MC = 3;
    final int UO = 4;
    String username, userSchool;
    RelativeLayout subletBoard, serviceBoard,eventBoard, sportBoard, exchangeBoard, carBoard,tutorBoard;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myFragmentView = inflater.inflate(R.layout.alltheboards, container, false);
        username = getArguments().getString("username");
        userSchool = getArguments().getString("school");
        System.out.println(userSchool);
        subletBoard = (RelativeLayout) myFragmentView.findViewById(R.id.subletBoard);
        serviceBoard = (RelativeLayout) myFragmentView.findViewById(R.id.serviceBoard);
        eventBoard = (RelativeLayout) myFragmentView.findViewById(R.id.eventBoard);
        sportBoard = (RelativeLayout) myFragmentView.findViewById(R.id.sportBoard);
        exchangeBoard = (RelativeLayout) myFragmentView.findViewById(R.id.exchangeBoard);
        carBoard = (RelativeLayout) myFragmentView.findViewById(R.id.carBoard);
        tutorBoard = (RelativeLayout) myFragmentView.findViewById(R.id.tutorBoard);

        subletBoard.setOnClickListener(this);
        serviceBoard.setOnClickListener(this);
        eventBoard.setOnClickListener(this);
        sportBoard.setOnClickListener(this);
        exchangeBoard.setOnClickListener(this);
        carBoard.setOnClickListener(this);
        tutorBoard.setOnClickListener(this);
        return myFragmentView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subletBoard:
                Intent intent = new Intent(getActivity().getBaseContext(),
                        subletBoard.class);
                intent.putExtra("username", username);
                intent.putExtra("school", userSchool);
                getActivity().startActivity(intent);
                break;
            case R.id.serviceBoard:
                Intent intent1 = new Intent(getActivity().getBaseContext(),
                        serviceBoard.class);
                intent1.putExtra("username", username);
                intent1.putExtra("school", userSchool);
                getActivity().startActivity(intent1);
                break;
            case R.id.tutorBoard:
                Intent intent2 = new Intent(getActivity().getBaseContext(),
                        tutorBoard.class);
                intent2.putExtra("username", username);
                intent2.putExtra("school", userSchool);
                getActivity().startActivity(intent2);
                break;
            case R.id.exchangeBoard:
                Intent intent3 = new Intent(getActivity().getBaseContext(),
                        exchangeBoard.class);
                intent3.putExtra("username", username);
                intent3.putExtra("school", userSchool);
                getActivity().startActivity(intent3);
                break;
            case R.id.eventBoard:
                Intent intent4 = new Intent(getActivity().getBaseContext(),
                        eventBoard.class);
                intent4.putExtra("username", username);
                intent4.putExtra("school", userSchool);
                getActivity().startActivity(intent4);
                break;
            case R.id.sportBoard:
                Intent intent5 = new Intent(getActivity().getBaseContext(),
                        sportBoard.class);
                intent5.putExtra("username", username);
                intent5.putExtra("school", userSchool);
                getActivity().startActivity(intent5);
                break;
            case R.id.carBoard:
                Intent intent6 = new Intent(getActivity().getBaseContext(),
                        carPoolBoard.class);
                intent6.putExtra("username", username);
                intent6.putExtra("school", userSchool);
                getActivity().startActivity(intent6);
                break;

        }
    }
}
