package layout;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inspira.cello.LibInspira;
import com.inspira.cello.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.inspira.cello.IndexInternal.global;
import static com.inspira.cello.IndexInternal.jsonObject;

//import android.app.Fragment;

public class SummaryPraOrderFragment extends Fragment implements View.OnClickListener{
//    private TextView tvCustomer, tvBroker, tvValuta, tvDate, tvSubtotal, tvGrandTotal, tvDiscNominal, tvPPNNominal;
//    private TextView tvPPN, tvDisc; //added by Tonny @17-Sep-2017  //untuk tampilan pada approval
//    private EditText etDisc, etPPN;
    private TextView tvCabang,tvSales,tvJenisHarga,tvKode,tvTanggal,tvCustomer,tvKeterangan,tvStatus,tvSetujuOleh,tvSetujuPada;
    private Button btnEdit;
    private InsertingData insertingData;

    public SummaryPraOrderFragment() {
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
        View v = inflater.inflate(R.layout.fragment_praorder_summary, container, false);
        getActivity().setTitle("Pra Order Summary");
        //tidak ganti title jika dipanggil pada saat approval atau disapproval
//        if(!LibInspira.getShared(global.temppreferences, global.temp.salesorder_type_task, "").equals("approval") &&
//                !LibInspira.getShared(global.temppreferences, global.temp.salesorder_type_task, "").equals("disapproval")) {
//            getActivity().setTitle("Sales Order Summary");
//        }else{  //added by Tonny @17-Sep-2017  //jika dipakai untuk approval, maka layout menggunakan fragment_summary_sales_order_approval untuk view saja
//            v = inflater.inflate(R.layout.fragment_summary_sales_order_approval, container, false);
//        }
        Log.d("sumasd","on create");
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
//        tvDate = (TextView) getView().findViewById(R.id.tvDate);
//        tvCustomer = (TextView) getView().findViewById(R.id.tvCustomer);
//        tvBroker = (TextView) getView().findViewById(R.id.tvBroker);
//        tvValuta = (TextView) getView().findViewById(R.id.tvValuta);
//        tvSubtotal = (TextView) getView().findViewById(R.id.tvSubtotal);
//        tvGrandTotal = (TextView) getView().findViewById(R.id.tvGrandTotal);
//        tvDiscNominal = (TextView) getView().findViewById(R.id.tvDiscNominal);
//        tvPPNNominal = (TextView) getView().findViewById(R.id.tvPPNNominal);

        tvCabang = (TextView) getView().findViewById(R.id.tvNamaCabang);
        tvSales = (TextView) getView().findViewById(R.id.tvNamaSales);
        tvJenisHarga = (TextView) getView().findViewById(R.id.tvJenisHarga);
        tvKode= (TextView) getView().findViewById(R.id.tvKode);
        tvTanggal= (TextView) getView().findViewById(R.id.tvTanggal);
        tvCustomer= (TextView) getView().findViewById(R.id.tvNamaKodeCustomer);
        tvKeterangan= (TextView) getView().findViewById(R.id.tvKeterangan);
        tvStatus= (TextView) getView().findViewById(R.id.tvStatus);
        tvSetujuOleh = (TextView) getView().findViewById(R.id.tvSetujuOleh);
        tvSetujuPada = (TextView) getView().findViewById(R.id.tvSetujuPada);

        btnEdit = (Button) getView().findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(this);

        loadDataFromShared();

        //### reset submenu handle ketika back fragment dan, finish button di formNewPraorderItemList
        LibInspira.setShared(global.temppreferences, global.temp.praorder_submenu, "");

        //Log.d("sumasd","activ created");




//        tvDate.setText(LibInspira.getShared(global.temppreferences, global.temp.salesorder_date, ""));
//        tvCustomer.setText(LibInspira.getShared(global.temppreferences, global.temp.salesorder_customer_nama, ""));
//        tvBroker.setText(LibInspira.getShared(global.temppreferences, global.temp.salesorder_broker_nama, ""));
//        tvValuta.setText(LibInspira.getShared(global.temppreferences, global.temp.salesorder_valuta_nama, ""));
//
//        if(!LibInspira.getShared(global.temppreferences, global.temp.salesorder_type_task, "").equals("approval") &&
//                !LibInspira.getShared(global.temppreferences, global.temp.salesorder_type_task, "").equals("disapproval")){
//            etPPN = (EditText) getView().findViewById(R.id.etPPN);
//            etDisc = (EditText) getView().findViewById(R.id.etDisc);
//            etDisc.setText("0");
//            etPPN.setText("0");
//            btnSave.setOnClickListener(this);
//            etDisc.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                    LibInspira.formatNumberEditText(etDisc, this, true, false);
//                    tvDiscNominal.setText(LibInspira.delimeter(getNominalDiskon().toString()));
//                    tvPPNNominal.setText(LibInspira.delimeter(getNominalPPN().toString()));
//                    tvGrandTotal.setText("Rp. " + LibInspira.delimeter(getGrandTotal().toString()));
//                    LibInspira.setShared(global.temppreferences, global.temp.salesorder_disc, etDisc.getText().toString().replace(",", ""));
//                    LibInspira.setShared(global.temppreferences, global.temp.salesorder_disc_nominal, tvDiscNominal.getText().toString().replace(",", ""));
//                }
//            });
//
//            etPPN.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                    LibInspira.formatNumberEditText(etPPN, this, true, false);
//                    tvPPNNominal.setText(LibInspira.delimeter(getNominalPPN().toString()));
//                    tvGrandTotal.setText("Rp. " + LibInspira.delimeter(getGrandTotal().toString()));
//                    LibInspira.setShared(global.temppreferences, global.temp.salesorder_ppn, etPPN.getText().toString().replace(",", ""));
//                    LibInspira.setShared(global.temppreferences, global.temp.salesorder_ppn_nominal, tvPPNNominal.getText().toString().replace(",", ""));
//                }
//            });
//
//            tvSubtotal.setText(LibInspira.delimeter(getSubtotal().toString()));
//            tvGrandTotal.setText("Rp. " + LibInspira.delimeter(getGrandTotal().toString()));
//
//            if(LibInspira.getShared(global.temppreferences, global.temp.salesorder_type_task, "").equals("nonppn"))
//            {
//                getView().findViewById(R.id.trPPN).setVisibility(View.GONE);
//            }
//
//        }else{  //added by Tonny @17-Sep-2017  jika untuk approval, beberapa property dihilangkan/diganti
//            tvPPN = (TextView) getView().findViewById(R.id.tvPPN);
//            tvDisc = (TextView) getView().findViewById(R.id.tvDisc);
//            btnSave.setVisibility(View.GONE);
//            tvSubtotal.setText(LibInspira.delimeter(LibInspira.getShared(global.temppreferences, global.temp.salesorder_subtotal, "")));
//            tvGrandTotal.setText("Rp. " + LibInspira.delimeter(LibInspira.getShared(global.temppreferences, global.temp.salesorder_total, "")));
//            tvDisc.setText(LibInspira.delimeter(LibInspira.getShared(global.temppreferences, global.temp.salesorder_disc, "")));
//            tvDiscNominal.setText(LibInspira.delimeter(LibInspira.getShared(global.temppreferences, global.temp.salesorder_disc_nominal, "")));
//            tvPPN.setText(LibInspira.delimeter(LibInspira.getShared(global.temppreferences, global.temp.salesorder_ppn, "")));
//            tvPPNNominal.setText(LibInspira.delimeter(LibInspira.getShared(global.temppreferences, global.temp.salesorder_ppn_nominal, "")));
//        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id==R.id.btnEdit)
        {
            // klo sdh di approve tdk isa di edit
            // klo belum di approve bisa di edit

            //BELUM DI BYPASS
            if(LibInspira.getShared(global.temppreferences, global.temp.praorder_selected_list_status, "").equals("1"))
            {
                //btnEdit.setVisibility(View.GONE);
                LibInspira.showLongToast(getActivity(),"Tidak bisa diedit karena sudah di APPROVE");
            }
            else
            {
                //btnEdit.setVisibility(View.VISIBLE);
                LibInspira.setShared(global.temppreferences, global.temp.praorder_menu, "edit");

                if(!LibInspira.getShared(global.temppreferences, global.temp.praorder_summary, "").equals(""))
                {
//                    LibInspira.setShared(global.temppreferences, global.temp.praorder_header_edit,
//                            LibInspira.getShared(global.temppreferences, global.temp.praorder_summary, ""));
                    trimDataShared(LibInspira.getShared(global.temppreferences, global.temp.praorder_summary, ""));
                }
                else{
                    LibInspira.showShortToast(getActivity(),"error load data header");
                }

                // di isi list dr item list
                if(!LibInspira.getShared(global.temppreferences, global.temp.praorder_item, "").equals("")) {
                    LibInspira.setShared(global.temppreferences, global.temp.praorder_item_add,
                            LibInspira.getShared(global.temppreferences, global.temp.praorder_item, ""));
                }
                else{
                    LibInspira.showShortToast(getActivity(),"error load data list items");
                }

                LibInspira.ReplaceFragment(getActivity().getSupportFragmentManager(), R.id.fragment_container, new FormNewPraOrderHeader());
            }
        }
//        else if(id==R.id.btnBack)
//        {
//            LibInspira.BackFragment(getActivity().getSupportFragmentManager());
//        }
    }

    public void trimDataShared(String data)
    {

//        data[0] = obj.getString("nomor");
//        data[1] = obj.getString("namaCabang");
//        data[2] = obj.getString("nomorSales");
//        data[3] = obj.getString("namaSales");
//
//        data[4] = obj.getString("nomorJenisHarga");
//        data[5] = obj.getString("namaJenisHarga");
//        data[6] = obj.getString("kode");
//        data[7] = obj.getString("tanggal");
//        data[8] = obj.getString("nomorCustomer");
//        data[9] = obj.getString("kodeCustomer");
//        data[10] = obj.getString("namaCustomer");
//
//        data[11] = obj.getString("ppnPersen");
//        data[12] = obj.getString("ppnNom");
//        data[13] = obj.getString("diskonPersen");
//        data[14] = obj.getString("diskonNom");
//        data[15] = obj.getString("kurs");
//
//        data[16] = obj.getString("keterangan");
//        data[17] = obj.getString("status_disetujui");
//
//        data[18] = obj.getString("disetujui_oleh");
//        data[19] = obj.getString("disetujui_pada");


        if(!data.equals(""))
        {
            String[] parts = data.trim().split("\\~");

            LibInspira.setShared(global.temppreferences, global.temp.praorder_header_nomor, parts[0]);

            LibInspira.setShared(global.temppreferences, global.temp.praorder_header_kode, parts[6]);

            LibInspira.setShared(global.temppreferences, global.temp.praorder_customer_nomor, parts[8]);
            LibInspira.setShared(global.temppreferences, global.temp.praorder_customer_nama, parts[10]);

            LibInspira.setShared(global.temppreferences, global.temp.praorder_sales_nomor, parts[2]);
            LibInspira.setShared(global.temppreferences, global.temp.praorder_sales_nama, parts[3]);

            LibInspira.setShared(global.temppreferences, global.temp.praorder_jenis_harga_nomor, parts[4]);
            LibInspira.setShared(global.temppreferences, global.temp.praorder_jenis_harga_nama, parts[5]);

            LibInspira.setShared(global.temppreferences, global.temp.praorder_date, parts[7].substring(0,10));
            LibInspira.setShared(global.temppreferences, global.temp.praorder_keterangan,parts[16] );
        }
    }

    private void loadDataFromShared()
    {
//        data[0] = obj.getString("nomor");
//        data[1] = obj.getString("namaCabang");
//        data[2] = obj.getString("nomorSales");
//        data[3] = obj.getString("namaSales");
//
//        data[4] = obj.getString("nomorJenisHarga");
//        data[5] = obj.getString("namaJenisHarga");
//        data[6] = obj.getString("kode");
//        data[7] = obj.getString("tanggal");
//        data[8] = obj.getString("nomorCustomer");
//        data[9] = obj.getString("kodeCustomer");
//        data[10] = obj.getString("namaCustomer");
//
//        data[11] = obj.getString("ppnPersen");
//        data[12] = obj.getString("ppnNom");
//        data[13] = obj.getString("diskonPersen");
//        data[14] = obj.getString("diskonNom");
//        data[15] = obj.getString("kurs");
//
//        data[16] = obj.getString("keterangan");
//        data[17] = obj.getString("status_disetujui");
//
//        data[18] = obj.getString("disetujui_oleh");
//        data[19] = obj.getString("disetujui_pada");

        //Log.d("sumasd","masuk");
        String data = LibInspira.getShared(global.temppreferences, global.temp.praorder_summary, "");
        Log.d("sumasd",data);
        //String[] pieces = data.trim().split("\\|");

        if(!data.equals(""))
        {
            String[] parts = data.trim().split("\\~");
            //        for(String k : parts) {
            //            Log.d("sumasd", k);
            //        }
            tvCabang.setText(parts[1]);
            tvSales.setText(parts[2] +" - "+ parts[3]);
            tvJenisHarga.setText(parts[5]);
            tvKode.setText(parts[6]);
            tvTanggal.setText(parts[7]);
            tvCustomer.setText(parts[9]+" - "+parts[10]);
            tvKeterangan.setText(parts[16]);
            if(parts[17].equals("1"))
            {
                tvStatus.setText("APPROVE");
            }
            else if(parts[17].equals("0"))
            {
                tvStatus.setText("DISAPPROVE");
                tvSetujuOleh.setVisibility(View.INVISIBLE);
                tvSetujuPada.setVisibility(View.INVISIBLE);
            }
            else
            {
                tvStatus.setText(parts[17]);
            }

            tvSetujuOleh.setText(parts[18]);
            tvSetujuPada.setText(parts[19]);
        }


//        for(int i=0 ; i < pieces.length ; i++){
//            Log.d("item", pieces[i] + "a");
//            if(!pieces[i].equals(""))
//            {


//                String nomor = parts[0];
//                String nama = parts[1];
//                String kode = parts[2];

//                if(nomor.equals("null")) nomor = "";
//                if(nama.equals("null")) nama = "";
//                if(kode.equals("null")) kode = "";
//            }
//        }

//        if(pieces.length==1)
//        {
//            //tvNoData.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            //tvNoData.setVisibility(View.GONE);
//        }
    }

    //added by Tonny @02-Sep-2017
    //untuk menjalankan perintah send data ke web service
    private void sendData(){
        String actionUrl = "Order/insertNewOrderJual/";
        insertingData = new InsertingData();
        insertingData.execute(actionUrl);
    }

    //added by Tonny @04-Sep-2017
    //class yang digunakan untuk insert data
    private class InsertingData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            jsonObject = new JSONObject();
            //---------------------------------------------HEADER-----------------------------------------------------//
            try {
                jsonObject.put("nomorcustomer", LibInspira.getShared(global.temppreferences, global.temp.salesorder_customer_nomor, ""));
                jsonObject.put("kodecustomer", LibInspira.getShared(global.temppreferences, global.temp.salesorder_customer_kode, ""));
                jsonObject.put("nomorbroker", LibInspira.getShared(global.temppreferences, global.temp.salesorder_broker_nomor, ""));
                jsonObject.put("kodebroker", LibInspira.getShared(global.temppreferences, global.temp.salesorder_broker_kode, ""));
                jsonObject.put("nomorsales", LibInspira.getShared(global.userpreferences, global.user.nomor_sales, ""));
                //jsonObject.put("kodesales", LibInspira.getShared(global.userpreferences, global.user.kode, ""));
                jsonObject.put("subtotal", LibInspira.getShared(global.temppreferences, global.temp.salesorder_subtotal, ""));
                jsonObject.put("subtotaljasa", LibInspira.getShared(global.temppreferences, global.temp.salesorder_pekerjaan_subtotal, ""));
                jsonObject.put("subtotalbiaya", LibInspira.getShared(global.temppreferences, global.temp.salesorder_subtotal_fee, ""));
                jsonObject.put("disc", LibInspira.getShared(global.temppreferences, global.temp.salesorder_disc, "0"));
                jsonObject.put("discnominal", LibInspira.getShared(global.temppreferences, global.temp.salesorder_disc_nominal, "0"));
                jsonObject.put("dpp", LibInspira.getShared(global.temppreferences, global.temp.salesorder_total, ""));
                jsonObject.put("ppn", LibInspira.getShared(global.temppreferences, global.temp.salesorder_ppn, "0"));
                jsonObject.put("ppnnominal", LibInspira.getShared(global.temppreferences, global.temp.salesorder_ppn_nominal, "0"));
                jsonObject.put("total", LibInspira.getShared(global.temppreferences, global.temp.salesorder_total, "0"));
                //jsonObject.put("totalrp", Double.toString(getGrandTotal() * Double.parseDouble(LibInspira.getShared(global.temppreferences, global.temp.salesorder_valuta_kurs, ""))));
                jsonObject.put("pembuat", LibInspira.getShared(global.userpreferences, global.user.nama, ""));
                jsonObject.put("nomorcabang", LibInspira.getShared(global.userpreferences, global.user.cabang, ""));
                jsonObject.put("cabang", LibInspira.getShared(global.temppreferences, global.user.namacabang, ""));
                jsonObject.put("valuta", LibInspira.getShared(global.temppreferences, global.temp.salesorder_valuta_nama, ""));
                jsonObject.put("kurs", LibInspira.getShared(global.temppreferences, global.temp.salesorder_valuta_kurs, ""));
                jsonObject.put("jenispenjualan", "Material");
                jsonObject.put("isbarangimport", LibInspira.getShared(global.temppreferences, global.temp.salesorder_import, ""));
                jsonObject.put("isppn", LibInspira.getShared(global.temppreferences, global.temp.salesorder_isPPN, ""));
                if(LibInspira.getShared(global.temppreferences, global.temp.salesorder_type_proyek, "").equals("proyek"))
                {
                    jsonObject.put("proyek", 1);
                }
                else if(LibInspira.getShared(global.temppreferences, global.temp.salesorder_type_proyek, "").equals("nonproyek"))
                {
                    jsonObject.put("proyek", 0);
                }
                jsonObject.put("user", LibInspira.getShared(global.userpreferences, global.user.nomor, ""));


                //-------------------------------------------------------------------------------------------------------//
                //---------------------------------------------DETAIL----------------------------------------------------//
                jsonObject.put("dataitemdetail", LibInspira.getShared(global.temppreferences, global.temp.salesorder_item, ""));  //mengirimkan data item
                jsonObject.put("datapekerjaandetail", LibInspira.getShared(global.temppreferences, global.temp.salesorder_pekerjaan, ""));  //mengirimkan data pekerjaan
                Log.d("detailitemdetail", LibInspira.getShared(global.temppreferences, global.temp.salesorder_item, ""));
                Log.d("detailpekerjaandetail", LibInspira.getShared(global.temppreferences, global.temp.salesorder_pekerjaan, ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return LibInspira.executePost(getContext(), urls[0], jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("resultQuery", result);
            try {
                JSONArray jsonarray = new JSONArray(result);
                if(jsonarray.length() > 0){
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        if(!obj.has("query")){
                            LibInspira.hideLoading();
                            LibInspira.showShortToast(getContext(), "Data has been successfully added");
                            LibInspira.clearShared(global.temppreferences); //hapus cache jika data berhasil ditambahkan
                            LibInspira.BackFragmentCount(getFragmentManager(), 6);  //kembali ke menu depan sales order
                        }
                        else
                        {
                            LibInspira.showShortToast(getContext(), "Adding new data failed");
                            LibInspira.hideLoading();
                        }
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                LibInspira.showShortToast(getContext(), "Adding new data failed");
                LibInspira.hideLoading();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LibInspira.showLoading(getContext(), "Inserting Data", "Loading");
            //tvInformation.setVisibility(View.VISIBLE);
        }
    }
}
