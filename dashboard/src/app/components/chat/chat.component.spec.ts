import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { ChatComponent } from './chat.component';
import { MessagesService } from 'src/app/services/messages/messages.service';
import { UserService } from 'src/app/services/user/user.service';

describe('ChatComponent', () => {
  let component: ChatComponent;
  let fixture: ComponentFixture<ChatComponent>;
  let messagesService: jasmine.SpyObj<MessagesService>;
  let userService: jasmine.SpyObj<UserService>;

  beforeEach(async () => {
    const messagesServiceSpy = jasmine.createSpyObj('MessagesService', [
      'getConversation',
      'sendMessageWebSocket',
      'sendMessageRest',
      'updateMessages',
      'markMessageAsRead'
    ]);
    const userServiceSpy = jasmine.createSpyObj('UserService', ['getUsers']);

    await TestBed.configureTestingModule({
      declarations: [ChatComponent],
      imports: [FormsModule],
      providers: [
        { provide: MessagesService, useValue: messagesServiceSpy },
        { provide: UserService, useValue: userServiceSpy }
      ]
    }).compileComponents();

    messagesService = TestBed.inject(MessagesService) as jasmine.SpyObj<MessagesService>;
    userService = TestBed.inject(UserService) as jasmine.SpyObj<UserService>;

    fixture = TestBed.createComponent(ChatComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load users on init', () => {
    const mockUsers = { data: [{ id: 2, nom: 'João', prenom: 'Silva' }] };
    userService.getUsers.and.returnValue(of(mockUsers));
    messagesService.getConversation.and.returnValue(of([]));

    fixture.detectChanges();

    expect(userService.getUsers).toHaveBeenCalled();
    expect(component.users.length).toBe(1);
  });

  it('should send message over WebSocket when connected', () => {
    component.selectedUserId = 2;
    component.newMessage = 'Hello';
    component.isConnected = true; // simulate websocket alive

    const mockMessage = {
      id: 1,
      senderId: 1,
      receiverId: 2,
      content: 'Hello',
      timestamp: new Date()
    };

    messagesService.sendMessageWebSocket.and.callFake((payload: any) => {
      // simulate receiving the message via websocket
      component.addMessageToList(mockMessage as any);
    });
    messagesService.getConversation.and.returnValue(of([]));

    component.sendMessage();

    expect(messagesService.sendMessageWebSocket).toHaveBeenCalled();
    expect(component.newMessage).toBe('');
  });

  it('should fallback to REST when disconnected', () => {
    component.selectedUserId = 2;
    component.newMessage = 'Hello';
    component.isConnected = false;

    const mockMessage = {
      id: 2,
      senderId: 1,
      receiverId: 2,
      content: 'Hello',
      timestamp: new Date()
    };

    messagesService.sendMessageRest.and.returnValue(of(mockMessage));
    messagesService.getConversation.and.returnValue(of([]));

    component.sendMessage();

    expect(messagesService.sendMessageRest).toHaveBeenCalled();
    expect(component.newMessage).toBe('');
  });

  it('should not send empty message', () => {
    component.selectedUserId = 2;
    component.newMessage = '   ';

    component.sendMessage();

    expect(messagesService.sendMessageWebSocket).not.toHaveBeenCalled();
    expect(messagesService.sendMessageRest).not.toHaveBeenCalled();
  });

  it('should format time correctly', () => {
    const date = new Date('2024-01-15T14:30:00');
    const formatted = component.formatTime(date);
    expect(formatted).toContain(':');
  });

  it('should identify own messages', () => {
    component.myUserId = 1;
    expect(component.isMyMessage(1)).toBe(true);
    expect(component.isMyMessage(2)).toBe(false);
});
});