package main;

import java.io.File;

/**
 * @author liuruihao
 * @create 2020/6/1 17:56
 */
public class Client {

    public static void main(String[] args) {
        String path = "D:\\workingPath\\ESB\\data\\req\\out";
        deleteDir(path);
    }

    public static boolean deleteDir(String path) {
        File file = new File(path);
        if (!file.exists()) {//�ж��Ƿ��ɾ��Ŀ¼�Ƿ����
            System.err.println("The dir are not exists!");
            return false;
        }

        String[] content = file.list();//ȡ�õ�ǰĿ¼�������ļ����ļ���
        for (String name : content) {
            File temp = new File(path, name);
            if (temp.isDirectory()) {//�ж��Ƿ���Ŀ¼
                deleteDir(temp.getAbsolutePath());//�ݹ���ã�ɾ��Ŀ¼�������
//                temp.delete();//ɾ����Ŀ¼
            } else {
                if (!temp.delete()) {//ֱ��ɾ���ļ�
                    System.err.println("Failed to delete " + name);
                }
            }
        }
        return true;
    }
}
