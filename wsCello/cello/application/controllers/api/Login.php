<?php

defined('BASEPATH') OR exit('No direct script access allowed');

// This can be removed if you use __autoload() in config.php OR use Modular Extensions
require APPPATH . '/libraries/REST_Controller.php';

/**
 * This is an example of a few basic event interaction methods you could use
 * all done with a hardcoded array
 *
 * @package         CodeIgniter
 * @subpackage      Rest Server
 * @category        Controller
 * @author          Phil Sturgeon, Chris Kacerguis
 * @license         MIT
 * @link            https://github.com/chriskacerguis/codeigniter-restserver
 */
class Login extends REST_Controller { 

    function __construct()
    {
        // Construct the parent class
        parent::__construct();

        // Configure limits on our controller methods
        // Ensure you have created the 'limits' table and enabled 'limits' within application/config/rest.php
        $this->methods['event_post']['limit'] = 500000000; // 500 requests per hour per event/key
        // $this->methods['event_delete']['limit'] = 50; // 50 requests per hour per event/key
        $this->methods['event_get']['limit'] = 500000000; // 500 requests per hour per event/key

        header("Access-Control-Allow-Origin: *");
        header("Access-Control-Allow-Methods: GET, POST");
        header("Access-Control-Allow-Headers: Origin, Content-Type, Accept, Authorization");
    }

	function ellipsis($string) {
        $cut = 30;
        $out = strlen($string) > $cut ? substr($string,0,$cut)."..." : $string;
        return $out;
    }
	
    function clean($string) {
        return preg_replace("/[^[:alnum:][:space:]]/u", '', $string); // Replaces multiple hyphens with single one.
    }

    function error($string) {
        return str_replace( array("\t", "\n", "\r") , " ", $string);
    }
	
	function getGCMId($user_nomor){
        $query = "  SELECT 
                    a.gcmid
                    FROM whuser_mobile a 
                    WHERE a.status_aktif > 0 AND (a.gcmid <> '' AND a.gcmid IS NOT NULL) AND a.nomor = $user_nomor ";
        return $this->db->query($query)->row()->gcmid;
    }

    public function send_gcm($registrationId,$message,$title,$fragment,$nomor,$nama)
    {
        $this->load->library('gcm');

        $this->gcm->setMessage($message);
        $this->gcm->setTitle($title);
        $this->gcm->setFragment($fragment);
        $this->gcm->setNomor($nomor);
        $this->gcm->setNama($nama);

        $this->gcm->addRecepient($registrationId);

        $this->gcm->setData(array(
            'some_key' => 'some_val'
        ));

        $this->gcm->setTtl(500);
        $this->gcm->setTtl(false);

        $this->gcm->setGroup('Test');
        $this->gcm->setGroup(false);

        $this->gcm->send();

       if ($this->gcm->send())
           echo 'Success for all messages';
       else
           echo 'Some messages have errors';

       print_r($this->gcm->status);
       print_r($this->gcm->messagesStatuses);

        die(' Worked.');
    }

	public function send_gcm_group($registrationId,$message,$title,$fragment,$nomor,$nama)
    {
        $this->load->library('gcm');

        $this->gcm->setMessage($message);
        $this->gcm->setTitle($title);
        $this->gcm->setFragment($fragment);
        $this->gcm->setNomor($nomor);
        $this->gcm->setNama($nama);

        foreach ($registrationId as $regisID) {
            $this->gcm->addRecepient($regisID);
        }

        $this->gcm->setTtl(500);
        $this->gcm->setTtl(false);

        $this->gcm->setGroup('Test');
        $this->gcm->setGroup(false);

        $this->gcm->send();
    }
	
	function test_get()
	{
		
		$result = "a";
		
		$data['data'] = array();

		$new_message ="lalalalala";

		// START SEND NOTIFICATION
        $vcGCMId = 'f9SX7qjc2u0:APA91bHQBkMgwwOenJx8kujj4M0-0UxQ_R7QgU0HvhwGpIyzEKo8O0QXll7S_nC7TFypjersrOpWKcGhwnn7pQNTClnDX4_GlXrSCLA8RBeqhARpXstN7duufMU0uEWuMe9cJ2W7bz1k';

        $this->send_gcm($vcGCMId, $this->ellipsis($new_message),'New Message(s) From ','PrivateMessage','0','0');
		$this->response($vcGCMId);
	}


	// --- POST Login --- //
	function loginUser_post()
	{
        $data['data'] = array();

        $value = file_get_contents('php://input');
		$jsonObject = (json_decode($value , true));

        $user = (isset($jsonObject["username"]) ? $this->clean($jsonObject["username"])     : "");
        $pass = md5((isset($jsonObject["password"]) ? $this->clean($jsonObject["password"]) : ""));
        $token = (isset($jsonObject["token"]) ? $jsonObject["token"]     : "");

        //print_r($user.$pass);

//        $interval  = $this->db->query("SELECT intnilai FROM whsetting_mobile WHERE intNomor = 1 LIMIT 1")->row()->intnilai;
//        $radius    = $this->db->query("SELECT intnilai FROM whsetting_mobile WHERE intNomor = 2 LIMIT 1")->row()->intnilai;
//        $tracking  = $this->db->query("SELECT intnilai FROM whsetting_mobile WHERE intNomor = 3 LIMIT 1")->row()->intnilai;
//        $jam_awal  = $this->db->query("SELECT intnilai FROM whsetting_mobile WHERE intNomor = 6 LIMIT 1")->row()->intnilai;
//        $jam_akhir = $this->db->query("SELECT intnilai FROM whsetting_mobile WHERE intNomor = 7 LIMIT 1")->row()->intnilai;

        $query = "  UPDATE mhadmin a
                    SET hash = UUID(),
                    gcm_id = '$token'
                    WHERE a.status_aktif > 0
                    AND a.kode = ?
                    AND BINARY a.sandi= ?";
        $this->db->query($query, array($user, $pass));

//        $query = "	SELECT
//						a.nomor AS nomor,
//						a.nomor AS nomor_android,
//						a.userType AS user_tipe,
//						#a.nomormhadmingrup AS usertipe,
//						a.sandi AS `password`,
//						a.nomormhsales AS nomor_sales,
//						c.kode AS kode_sales,
//						a.nama AS nama,
//						a.nomormhadmingrup AS role,
//						a.hash AS `hash`,
//						IFNULL(c.hp, '') AS telp,
//						a.nomormhcabang AS cabang,
//						d.nama AS namacabang,
//						d.kode as kodecabang,
//						b.mobile_isowner AS isowner,
//						b.mobile_issales AS issales,
//						b.mobile_setting AS setting,
//						b.mobile_settingtarget AS settingtarget,
//						b.mobile_salesorder AS salesorder,
//						b.mobile_stockmonitoring AS stockmonitoring,
//						b.mobile_pricelist AS pricelist,
//						b.mobile_addscheduletask AS addscheduletask,
//						b.mobile_salestracking AS salestracking,
//						b.mobile_hpp AS hpp,
//						b.mobile_crossbranch AS crossbranch,
//						b.mobile_creategroup AS creategroup
//					FROM mhadmin a
//					JOIN mhadmingrup b ON a.nomormhadmingrup = b.nomor
//					LEFT JOIN mhsales c ON a.nomormhsales = c.nomor
//					JOIN mhcabang d ON a.nomormhcabang = d.nomor
//					WHERE a.status_aktif = 1
//					AND a.kode = '$user'
//					AND BINARY a.sandi = '$pass'";

        $query = "	SELECT
                        a.nomor AS nomor,
                        a.sandi AS `password`,
                        a.nomormhsales AS nomor_sales,
                        c.kode AS kode_sales,
                        a.nama AS nama,
                        a.nomormhadmingrup AS role,
                        b.nama AS namaadmingrup,
                        a.hash AS `hash`,
                        a.nomormhcabang AS cabang,
                        d.kode AS kodecabang,
                        d.nama AS namacabang
                    FROM mhadmin a
                    JOIN mhadmingrup b ON a.nomormhadmingrup = b.nomor
                    LEFT JOIN mhsales c ON a.nomormhsales = c.nomor
                    JOIN mhcabang d ON a.nomormhcabang = d.nomor
                    WHERE a.status_aktif = 1
                        AND a.kode = '$user'
                        AND BINARY a.sandi = '$pass'";
        $result = $this->db->query($query, array($user, $pass));
        if($result){
            if($result->num_rows() > 0){
                foreach ($result->result_array() as $r){
                    array_push($data['data'], array(
                                                    'user_nomor'                    => $r['nomor'],
                                                    'user_password'					=> $r['password'],
                                                    'user_nomor_sales'         		=> $r['nomor_sales'],
                                                    'user_kode_sales'         		=> $r['kode_sales'],
                                                    'user_nama' 					=> $r['nama'],
                                                    'user_role' 					=> $r['role'],
                                                    'user_nama_admin_grup'          => $r['namaadmingrup'],
                                                    'user_hash' 					=> $r['hash'],
                                                    'user_cabang' 					=> $r['cabang'],
                                                    'user_kode_cabang' 				=> $r['kodecabang'],
                                                    'user_nama_cabang' 				=> $r['namacabang']
                                                    )
                    );
                }
            }else{
                array_push($data['data'], array( 'message' => "No user found"));
            }
        }else{
			array_push($data['data'], array( 'query' => $this->error($query) ));
		}

        if ($data){
            // Set the response and exit
            $this->response($data['data']); // OK (200) being the HTTP response code
        }
    }

	// --- POST GPS tracking system --- //
	function gpsTracking_post()
	{
        $data['data'] = array();

        $value = file_get_contents('php://input');
		$jsonObject = (json_decode($value , true));

        array_push($data['data'], array(
			'id'				=> $r['id'],
			'latitude'			=> $r['latitude'],
			'longitude'			=> $r['longitude'],
			)
		);

        if ($data){
            // Set the response and exit
            $this->response($data['data']); // OK (200) being the HTTP response code
        }

    }

    function getVersion_post(){

        $data['data'] = array();

        $value = file_get_contents('php://input');
        $jsonObject = (json_decode($value , true));

        $version  = $this->db->query("SELECT a.version FROM whversion_mobile a ORDER BY nomor DESC LIMIT 1")->row()->version;
        $url      = $this->db->query("SELECT a.url FROM whversion_mobile a ORDER BY nomor DESC LIMIT 1")->row()->url;

        array_push($data['data'], array(
                                        'version' 	=> $version,
                                        'url'	=> $url
                                        )
        );

        if ($data){
            // Set the response and exit
            $this->response($data['data']); // OK (200) being the HTTP response code
        }
    }

    function checkUser_post()
	{
        $data['data'] = array();
		$value = file_get_contents('php://input');
		$jsonObject = (json_decode($value , true));

        $hash = (isset($jsonObject["hash"]) ? $jsonObject["hash"]     : "");
//        $interval  = $this->db->query("SELECT intnilai FROM whsetting_mobile WHERE intNomor = 1 LIMIT 1")->row()->intnilai;
//        $radius    = $this->db->query("SELECT intnilai FROM whsetting_mobile WHERE intNomor = 2 LIMIT 1")->row()->intnilai;
//		$tracking  = $this->db->query("SELECT intnilai FROM whsetting_mobile WHERE intNomor = 3 LIMIT 1")->row()->intnilai;
//		$jam_awal  = $this->db->query("SELECT intnilai FROM whsetting_mobile WHERE intNomor = 6 LIMIT 1")->row()->intnilai;
//		$jam_akhir = $this->db->query("SELECT intnilai FROM whsetting_mobile WHERE intNomor = 7 LIMIT 1")->row()->intnilai;
//        $query = "	SELECT
//						a.nomor AS nomor,
//						a.nomor AS nomor_android,
//						a.userType AS user_tipe,
//						a.sandi AS `password`,
//						a.nomormhsales AS nomor_sales,
//						c.kode AS kode_sales,
//						a.nama AS nama,
//						a.nomormhadmingrup AS role,
//						a.hash AS `hash`,
//						IFNULL(c.hp, '') AS telp,
//						a.nomormhcabang AS cabang,
//						d.nama AS namacabang,
//						d.kode as kodecabang,
//						b.mobile_isowner AS isowner,
//						b.mobile_issales AS issales,
//						b.mobile_setting AS setting,
//						b.mobile_settingtarget AS settingtarget,
//						b.mobile_salesorder AS salesorder,
//						b.mobile_stockmonitoring AS stockmonitoring,
//						b.mobile_pricelist AS pricelist,
//						b.mobile_addscheduletask AS addscheduletask,
//						b.mobile_salestracking AS salestracking,
//						b.mobile_hpp AS hpp,
//						b.mobile_crossbranch AS crossbranch,
//						b.mobile_creategroup AS creategroup
//					FROM mhadmin a
//					JOIN mhadmingrup b ON a.nomormhadmingrup = b.nomor
//					LEFT JOIN mhsales c ON a.nomormhsales = c.nomor
//					JOIN mhcabang d ON a.nomormhcabang = d.nomor
//					WHERE a.status_aktif = 1
//						AND hash = '$hash'";

        $query = "	SELECT
                        a.nomor AS nomor,
                        a.sandi AS `password`,
                        a.nomormhsales AS nomor_sales,
                        c.kode AS kode_sales,
                        a.nama AS nama,
                        a.nomormhadmingrup AS role,
                        b.nama AS namaadmingrup,
                        a.hash AS `hash`,
                        a.nomormhcabang AS cabang,
                        d.kode AS kodecabang,
                        d.nama AS namacabang
                    FROM mhadmin a
                    JOIN mhadmingrup b ON a.nomormhadmingrup = b.nomor
                    LEFT JOIN mhsales c ON a.nomormhsales = c.nomor
                    JOIN mhcabang d ON a.nomormhcabang = d.nomor
                    WHERE a.status_aktif = 1
                        AND a.hash = '$hash' ";
        $result = $this->db->query($query);

        if($result){
            if($result->num_rows() > 0){
                foreach ($result->result_array() as $r){
                    array_push($data['data'], array(
                                                    'user_nomor'                    => $r['nomor'],
                                                    'user_password'					=> $r['password'],
                                                    'user_nomor_sales'         		=> $r['nomor_sales'],
                                                    'user_kode_sales'         		=> $r['kode_sales'],
                                                    'user_nama' 					=> $r['nama'],
                                                    'user_role' 					=> $r['role'],
                                                    'user_nama_admin_grup'          => $r['namaadmingrup'],
                                                    'user_hash' 					=> $r['hash'],
                                                    'user_cabang' 					=> $r['cabang'],
                                                    'user_kode_cabang' 				=> $r['kodecabang'],
                                                    'user_nama_cabang' 				=> $r['namacabang']
                                                    )
                    );
                }
            }else{
                array_push($data['data'], array( 'message' => "No user found"));
            }
        }else{
            array_push($data['data'], array( 'query' => $this->error($query) ));
        }

        if ($data){
            // Set the response and exit
            $this->response($data['data']); // OK (200) being the HTTP response code
        }
    }
}
