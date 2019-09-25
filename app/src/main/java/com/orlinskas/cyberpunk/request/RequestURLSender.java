package com.orlinskas.cyberpunk.request;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RequestURLSender {
    public String send(URL url) throws IOException {
    HttpURLConnection httpURLConnection = null;

    try {
        httpURLConnection = (HttpURLConnection) url.openConnection();
        InputStream in = httpURLConnection.getInputStream();
        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");

        boolean hasInput = scanner.hasNext();
        if (hasInput) {
            return scanner.next();
        } else {
            return null;
        }
    }
    finally { if (httpURLConnection != null) {
        httpURLConnection.disconnect();
    }
    }
  }
}

