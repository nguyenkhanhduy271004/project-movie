package nguyenduy.local.movie.helper;

public class CustomMessage {

  public static String createSuccess(String data) {
    return "Thêm " + data + " thành công!";
  }

  public static String updateSuccess(String data) {
    return "Cập nhật " + data + " thành công!";
  }

  public static String getSuccess(String data) {
    return "Lấy dữ liệu " + data + " thành công!";
  }

  public static String getWithIdSuccess(Long id, String data) {
    return "Lấy dữ liệu " + data + " với id: " + id + " thành công!";
  }

  public static String deleteSuccess(String data) {
    return "Xóa " + data + " thành công!";
  }

  public static String createFailed(String data) {
    return "Thêm " + data + " thất bại!";
  }

  public static String updateFailed(String data) {
    return "Cập nhật " + data + " thất bại!";
  }

  public static String getFailed(String data) {
    return "Lấy dữ liệu " + data + " thất bại!";
  }

  public static String deleteFailed(String data) {
    return "Xóa " + data + " thất bại!";
  }



}
