package accommodation;

import javax.persistence.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

@Entity
@Table(name="RoomManagement_table")
public class Room {

    @Id @GeneratedValue
    private Integer roomNo;
    private String roomType;
    private String roomStatus;
    private String roomName;
    private Integer roomQty;
    private String roomPrice;

    @PostPersist
    public void onPostPersist(){
        RoomConditionChanged roomConditionChanged = new RoomConditionChanged();
        roomConditionChanged.setRoomNo(this.getRoomNo());
        roomConditionChanged.setRoomType(this.getRoomType());
        roomConditionChanged.setRoomStatus(this.getRoomStatus());
        roomConditionChanged.setRoomName(this.getRoomName());
        roomConditionChanged.setRoomQty(this.getRoomQty());
        roomConditionChanged.setRoomPrice(this.getRoomPrice());

        BeanUtils.copyProperties(this, roomConditionChanged);

        roomConditionChanged.publishAfterCommit();
    }
    @PostUpdate
    public void onPostUpdate() {
        System.out.println("예약가능?:"+this.roomStatus);
        RoomConditionChanged roomConditionChanged = new RoomConditionChanged();
        roomConditionChanged.setRoomNo(this.getRoomNo());
        roomConditionChanged.setRoomType(this.getRoomType());
        roomConditionChanged.setRoomStatus(this.getRoomStatus());
        roomConditionChanged.setRoomName(this.getRoomName());
        roomConditionChanged.setRoomQty(this.getRoomQty());
        roomConditionChanged.setRoomPrice(this.getRoomPrice());

        BeanUtils.copyProperties(this, roomConditionChanged);
        roomConditionChanged.publishAfterCommit();
        System.out.println("예약가능으로 변경");
    }

    public Integer getRoomNo() {
        return roomNo;
    }
    public void setRoomNo(Integer roomNo) {
        this.roomNo = roomNo;
    }
    public String getRoomStatus() {
        return roomStatus;
    }
    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }
    public String getRoomName() {
        return roomName;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getRoomQty() {
        return roomQty;
    }

    public void setRoomQty(Integer roomQty) {
        this.roomQty = roomQty;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }
}
