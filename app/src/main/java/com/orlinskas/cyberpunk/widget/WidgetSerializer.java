package com.orlinskas.cyberpunk.widget;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class WidgetSerializer {
    private static final String FILE_WIDGETS_LIST_NAME = "widgetsList" + ".txt";
    private Context context;

        WidgetSerializer(Context context){
            this.context = context;
        }

        WidgetsList loadList() throws Exception {
            String filePath = context.getFilesDir().getPath() + "/" + FILE_WIDGETS_LIST_NAME;
            File file = new File(filePath);

            if(file.exists() && !file.isDirectory()) {
                ObjectInputStream ois =
                        new ObjectInputStream(new FileInputStream(file));

                return (WidgetsList) ois.readObject();
            }
            else return new WidgetsList();
        }

        void saveList(WidgetsList widgetsList) throws Exception {
            String filePath = context.getFilesDir().getPath() + "/" + FILE_WIDGETS_LIST_NAME;
            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(widgetsList);
        }
}
