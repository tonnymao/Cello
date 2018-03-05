/******************************************************************************
    Author           : ADI
    Description      : dashboard untuk internal
    History          :

******************************************************************************/
package layout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inspira.babies.LibInspira;
import com.inspira.babies.R;

import static com.inspira.babies.IndexExternal.global;

//import android.app.Fragment;

public class DashboardExternalFragment extends Fragment implements View.OnClickListener{
    public DashboardExternalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard_external, container, false);
        getActivity().setTitle("Dashboard");
        return v;
    }


    /*****************************************************************************/
    //OnAttach dijalankan pada saat fragment ini terpasang pada Activity penampungnya
    /*****************************************************************************/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    //added by Tonny @15-Jul-2017
    //untuk mapping UI pada fragment, jangan dilakukan pada OnCreate, tapi dilakukan pada onActivityCreated
    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        getView().findViewById(R.id.btnCatalogue).setOnClickListener(this);
        getView().findViewById(R.id.btnCart).setOnClickListener(this);
        getView().findViewById(R.id.btnAccountReceivableReport).setOnClickListener(this);
        getView().findViewById(R.id.btnTrackingInformation).setOnClickListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id==R.id.btnCatalogue)
        {
            LibInspira.setShared(global.sharedpreferences, global.shared.position, "pricelist");
            LibInspira.ReplaceFragment(getActivity().getSupportFragmentManager(), R.id.fragment_container, new ChooseJenisFragment());
        }
        else if(id==R.id.btnCart)
        {

        }
        else if(id==R.id.btnTrackingInformation)
        {

        }
        else if(id==R.id.btnAccountReceivableReport)
        {

        }
    }
}
