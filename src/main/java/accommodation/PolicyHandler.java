package accommodation;

import accommodation.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired
    RoomRepository roomManagementRepository;
    
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCheckedOut_객실정비요청(@Payload CheckedOut checkedOut) throws InterruptedException {

        if(checkedOut.isMe()){
            System.out.println("##### listener 객실정비요청 : " + checkedOut.toJson());

            if(roomManagementRepository.findById(checkedOut.getRoomNumber()) != null && "checkOut".equals(checkedOut.getReserveStatus())){
                Room room = new Room();
                room.setRoomNo(checkedOut.getRoomNumber());
                /*roomManagement.setRoomStatus("정비중");

                TimeUnit.MINUTES.sleep(1);
                roomManagementRepository.save(roomManagement);*/
                //정비 1분 소요 후 예약가능 상태로 변경
                room.setRoomStatus("RoomAvailable");
                roomManagementRepository.save(room);
            }
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReserved_객실상태변경(@Payload Reserved reserved){
        if(reserved.isMe()){
            System.out.println("##### listener 객실상태변경 : " + reserved.toJson());
            if(roomManagementRepository.findById(reserved.getRoomNumber()) != null && "reserve".equals(reserved.getReserveStatus())){
                Room room = new Room();
                room.setRoomNo(reserved.getRoomNumber());
                room.setRoomStatus("RoomNotAvaliable");
                roomManagementRepository.save(room);
            }
        }
    }
}
