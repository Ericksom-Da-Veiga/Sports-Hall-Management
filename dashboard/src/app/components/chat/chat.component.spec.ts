import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { ChatComponent } from './chat.component';
import { MessagesService } from 'src/app/services/messages/messages.service';
import { UserService } from 'src/app/services/user/user.service';
import { AuthService } from 'src/app/services/auth/auth.service';

describe('ChatComponent', () => {
  let component: ChatComponent;
  let fixture: ComponentFixture<ChatComponent>;
  let messagesService: jasmine.SpyObj<MessagesService>;
  let userService: jasmine.SpyObj<UserService>;
  let authService: jasmine.SpyObj<any>;

  beforeEach(async () => {
    const messagesServiceSpy = jasmine.createSpyObj('MessagesService', [
      'getConversation',
      'sendMessageWebSocket',
      'sendMessageRest',
      'updateMessages',
      'markMessageAsRead'
    ]);
    const userServiceSpy = jasmine.createSpyObj('UserService', ['getUsers']);
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['getUserFromToken']);

    await TestBed.configureTestingModule({
      declarations: [ChatComponent],
      imports: [FormsModule],
      providers: [
        { provide: MessagesService, useValue: messagesServiceSpy },
        { provide: UserService, useValue: userServiceSpy },
        { provide: AuthService, useValue: authServiceSpy }
      ]
    }).compileComponents();

    messagesService = TestBed.inject(MessagesService) as jasmine.SpyObj<MessagesService>;
    userService = TestBed.inject(UserService) as jasmine.SpyObj<UserService>;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<any>;

    fixture = TestBed.createComponent(ChatComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load users on init and determine own id from token email', () => {
    const mockUsers = { data: [
      { id: 1, mail: 'admin@admin.com', nom: 'Admin', prenom: 'User' },
      { id: 2, mail: 'j.silva@example.com', nom: 'João', prenom: 'Silva' }
    ] };
    // token contains sub=email; component should match it to user with id 1
    authService.getUserFromToken.and.returnValue({ sub: 'ADMIN@admin.com' });

    userService.getUsers.and.returnValue(of(mockUsers));
    messagesService.getConversation.and.returnValue(of([]));

    spyOn(console, 'debug');

    fixture.detectChanges();

    expect(userService.getUsers).toHaveBeenCalled();
    expect(component.myUserId).toBe(1);
    // after filtering out self only one user remains
    expect(component.users.length).toBe(1);
    expect(component.users[0].id).toBe(2);
    // conversation should be fetched for the remaining user
    expect(messagesService.getConversation).toHaveBeenCalledWith(2);
    expect(console.debug).toHaveBeenCalledWith('loadConversation para', jasmine.any(Object));
  });

  it('should fall back to numeric parse if email match fails', () => {
    // token contains email that does not match any user from the API
    authService.getUserFromToken.and.returnValue({ sub: 'unknown@example.com' });
    const mockUsers = { data: [{ id: 2, mail: 'j.silva@example.com', nom: 'João', prenom: 'Silva' }] };
    userService.getUsers.and.returnValue(of(mockUsers));
    messagesService.getConversation.and.returnValue(of([]));

    fixture.detectChanges();

    // our fallback logic tries to convert sub to a number, fails, and sets 1
    expect(component.myUserId).toBe(1);
  });

  it('should pick up email from non-sub claim and not filter when id unknown', () => {
    authService.getUserFromToken.and.returnValue({ email: 'notfound@example.com' });
    const mockUsers = { data: [
      { id: 1, mail: 'admin@admin.com', nom: 'Admin', prenom: 'User' },
      { id: 2, mail: 'j.silva@example.com', nom: 'João', prenom: 'Silva' }
    ] };
    userService.getUsers.and.returnValue(of(mockUsers));
    messagesService.getConversation.and.returnValue(of([]));

    fixture.detectChanges();

    // email 'notfound' didn't match any, numeric parse also not available,
    // so myUserId remains null and we should have both users in list.
    expect(component.myUserId).toBeNull();
    expect(component.users.length).toBe(2);
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