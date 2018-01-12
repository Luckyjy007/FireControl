package org.chinalbs.spark;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordWriter;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;
import org.apache.hadoop.util.Progressable;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by apple on 2017/2/15.
 */
public class CorsMultipleTextOutputFormat<K, V> extends MultipleTextOutputFormat<K, V> {

    private TextOutputFormat<K, V> theTextOutputFormat = null;

    @Override
    public RecordWriter getRecordWriter(final FileSystem fs, final JobConf job, final String name, final Progressable arg3) throws IOException {

        return new RecordWriter() {
            TreeMap<String, RecordWriter<K, V>> recordWriters = new TreeMap();

            @Override
            public void write(Object key, Object value) throws IOException {
                //key中为消息内容，value无意义
                String pathName = "lucky";
                String val = "value";
                if (null == key || "" == key) {
                    pathName = "lucky";
                } else {
                    pathName = key.toString();
                }
                if (null == val || "" == val) {
                    val = "value";
                } else {
                    value = value.toString();
                }
                // 值为空不写入
//                if(!"".equals(pathName)){
//                    System.out.println(pathName+"line$$$$$$$$$$$$$$$$$$$"+val);
                //根据消息内容，定义输出路径和输出内容（同时清洗数据）
                //                    String[] ss = LineInterpret.interpretLine(line, "/test/spark/kafka");
                //                String[] ss = new String[2];
                //                ss[0] ="other";
                //                ss[1] = "pathtest";
                //name的最后两位为jobid，同一个文件只能同时允许一个job写入，多个job写一个文件会报错，将jobid作为文件名的一部分
                //能解决此问题
                String finalPath = pathName + "-" + name.substring(name.length() - 2) + "-" + "20171221";
                RecordWriter rw = (RecordWriter) this.recordWriters.get(finalPath);
                try {
                    if (rw == null) {
                        rw = getBaseRecordWriter(fs, job, finalPath, arg3);
                        this.recordWriters.put(finalPath, rw);
                    }
                    rw.write(val, null);
                } catch (Exception e) {
                    //一个周期内，job不能完成，下一个job启动，会造成同时写一个文件的情况，变更文件名，添加后缀
                    this.rewrite(finalPath + "-", val);
                }
//                }
            }

            public void rewrite(String path, String line) {
//                String finalPath = path + new Random().nextInt(10);
                String finalPath = path;
                RecordWriter rw = (RecordWriter) this.recordWriters.get(finalPath);
                try {
                    if (rw == null) {
                        rw = getBaseRecordWriter(fs, job, finalPath, arg3);
                        this.recordWriters.put(finalPath, rw);
                    }
                    rw.write(line, null);
                } catch (Exception e) {
                    //重试
                    this.rewrite(finalPath, line);
                }
            }

            @Override
            public void close(Reporter reporter) throws IOException {
                Iterator keys = this.recordWriters.keySet().iterator();
                while (keys.hasNext()) {
                    RecordWriter rw = (RecordWriter) this.recordWriters.get(keys.next());
                    rw.close(reporter);
                }
                this.recordWriters.clear();
            }
        };
    }

    @Override
    protected RecordWriter<K, V> getBaseRecordWriter(FileSystem fs, JobConf job, String path, Progressable arg3) throws IOException {
        if (this.theTextOutputFormat == null) {
//            this.theTextOutputFormat = new CorsTextOutputFormat();
            this.theTextOutputFormat = new TextOutputFormat();
        }
        return this.theTextOutputFormat.getRecordWriter(fs, job, path, arg3);
    }
}
