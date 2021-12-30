package com.oxf.mq.tt;
import java.io.*;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
public class OxfUtils {
    /**
     * 压缩
     */
    public static byte[] compress(byte[] data) {
        byte[] output = new byte[0];
        Deflater compresser = new Deflater();
        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!compresser.finished()) {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }
            output = bos.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        compresser.end();
        return output;
    }

    /**
     * 输出流压缩
     */
    public static void compress(byte[] data, OutputStream os) {
        DeflaterOutputStream dos = new DeflaterOutputStream(os);
        try {
            dos.write(data, 0, data.length);
            dos.finish();
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解压缩
     *
     * @param data
     *            待压缩的数据
     * @return byte[] 解压缩后的数据
     */
    public static byte[] decompress(byte[] data) {
        byte[] output = new byte[0];

        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);

        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!decompresser.finished()) {
                int i = decompresser.inflate(buf);
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                o.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        decompresser.end();
        return output;
    }

    /**
     * 解压缩
     *
     * @param is
     *            输入流
     * @return byte[] 解压缩后的数据
     */
    public static byte[] decompress(InputStream is) {
        InflaterInputStream iis = new InflaterInputStream(is);
        ByteArrayOutputStream o = new ByteArrayOutputStream(1024);
        try {
            int i = 1024;
            byte[] buf = new byte[i];

            while ((i = iis.read(buf, 0, i)) > 0) {
                o.write(buf, 0, i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return o.toByteArray();
    }


    public static void main(String[] args) throws IOException {
        System.out.println("字节压缩／解压缩测试");
        String inputStr = "params:{\"资产id\":\"ccdf9e9bcf1a40cc8254ef695e4ea2cb\",\"数据来源\":\"user\",\"字段类型key\":\"200-228\",\"字段名称\":\"使用人\",\"字段值\":\"95f861189f2a45f2bac76da7c07aa46b\",\"groupCode\":\"0000\",\"identificationKey\":\"ce97e69f5f0a4e978f2f348c6778817d\",\"是否基础数据源信息字段\":true}\"formId\":1102,\"ndId\":1,\"taskId\":10104,\"toId\":2004,\"vmDomainList\":[{\"address\":\"https://rdvcenter.iottepa.cn\",\"id\":2,\"isOpen\":0,\"pwd\":\"Wanwu0322\",\"status\":1,\"user\":\"x0104\",\"vmType\":0}]}";
        System.out.println("输入字符串（原）:" + inputStr);
        byte[] input = inputStr.getBytes();
        System.out.println("输入字节长度:（原）" + input.length);

        FileOutputStream fos = new FileOutputStream(new File("d://zlib.bin"));
        fos.write(input);
        fos.flush();
        fos.close();

        FileInputStream inputStream = new FileInputStream(new File("d://zlib.bin"));
        byte[] data = OxfUtils.compress(inputStream.readAllBytes());
        inputStream.close();

        //byte[] data = ZLibUtils.compress(input);
        System.out.println("压缩后字节长度:" + data.length);

        byte[] output = OxfUtils.decompress(data);
        System.out.println("解压缩后字节长度(还原):" + output.length);
        String outputStr = new String(output);
        System.out.println("输出字符串:(还原)" + outputStr);
    }
}
