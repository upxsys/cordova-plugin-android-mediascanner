import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import android.util.Log;
import android.provider.Settings;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;
import android.os.Environment;
import java.lang.InterruptedException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MediaScanner extends CordovaPlugin implements MediaScannerConnection.MediaScannerConnectionClient {

	private MediaScannerConnection scanner;
	private String filePath;

	public boolean execute(final String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
		if(action.equals("mediaScanner")) {
			Log.i("Call", "OK, on tente un scan...");

			try {
				filePath = args.getString(0);		//_Get the path of the media to scan

				MediaScannerConnection.scanFile(this.cordova.getActivity().getApplicationContext(),
                    new String[] { filePath },
                    null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                                if(uri == null){
                                    callbackContext.error("error");
                                }else{
                                    callbackContext.success();
                                }
                            }
                });

				// return this.mediaScanner(filePath, callbackContext);
			} catch (JSONException e) {
				e.printStackTrace();
		        	callbackContext.error(e.getMessage());
		        return false;
			}
		} else {
			return false;
		}
		return false;
	}

	private boolean mediaScanner(String mediaPath, CallbackContext callback) {
		scanner = new MediaScannerConnection(this.cordova.getActivity().getApplicationContext(), MediaScanner.this);
		scanner.connect();

		return true;
	}

    @Override
    public void onMediaScannerConnected() {
        System.out.println("SCAN !");
        scanner.scanFile(filePath, null);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        scanner.disconnect();
    }
}
