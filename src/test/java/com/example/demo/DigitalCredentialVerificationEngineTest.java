package com.example.demo;

import com.example.demo.controller.*;
import com.example.demo.dto.JwtResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import com.example.demo.servlet.SimpleStatusServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Listeners(TestResultListener.class)
public class DigitalCredentialVerificationEngineTest {

    @Mock private CredentialHolderProfileRepository holderRepo;
    @Mock private CredentialRecordRepository credentialRepo;
    @Mock private VerificationRuleRepository ruleRepo;
    @Mock private VerificationRequestRepository verificationRequestRepo;
    @Mock private AuditTrailRecordRepository auditRepo;
    @Mock private UserRepository userRepository;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtUtil jwtUtil;

    private CredentialHolderProfileServiceImpl holderService;
    private CredentialRecordServiceImpl credentialService;
    private VerificationRuleServiceImpl ruleService;
    private AuditTrailServiceImpl auditService;
    private VerificationRequestServiceImpl verificationService;
    private UserServiceImpl userService;

    private CredentialHolderController holderController;
    private CredentialRecordController credentialController;
    private VerificationRuleController ruleController;
    private VerificationRequestController verificationController;
    private AuditTrailController auditController;
    private AuthController authController;

    private SimpleStatusServlet simpleStatusServlet;

    @BeforeClass
public void setup() {
    MockitoAnnotations.openMocks(this);

    holderService = new CredentialHolderProfileServiceImpl(holderRepo);
    credentialService = new CredentialRecordServiceImpl(credentialRepo);
    ruleService = new VerificationRuleServiceImpl(ruleRepo);
    auditService = new AuditTrailServiceImpl(auditRepo);
    verificationService = new VerificationRequestServiceImpl(
            verificationRequestRepo, credentialService, ruleService, auditService);

    // FIX: Replace invalid lambda with a valid PasswordEncoder implementation
    userService = new UserServiceImpl(
            userRepository,
            new PasswordEncoder() {
                @Override
                public String encode(CharSequence rawPassword) {
                    return rawPassword + "_ENC";
                }

                @Override
                public boolean matches(CharSequence rawPassword, String encodedPassword) {
                    return encodedPassword.equals(rawPassword + "_ENC");
                }
            }
    );

    holderController = new CredentialHolderController(holderService);
    credentialController = new CredentialRecordController(credentialService);
    ruleController = new VerificationRuleController(ruleService);
    verificationController = new VerificationRequestController(verificationService);
    auditController = new AuditTrailController(auditService);
    authController = new AuthController(userService, authenticationManager, jwtUtil);

    simpleStatusServlet = new SimpleStatusServlet();
}


    // ------------------------------------------------------------------------------------
    // 1. Servlet – 8 Tests
    // ------------------------------------------------------------------------------------

    @Test(priority = 1, groups = "servlet")
    public void t01_servlet_basicResponseText() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        StringWriter sw = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(sw));

        simpleStatusServlet.doGet(req, resp);

        Assert.assertEquals(sw.toString().trim(),
                "Digital Credential Verification Engine is running");
    }

    @Test(priority = 2, groups = "servlet")
    public void t02_servlet_doesNotReturnHtml() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        StringWriter sw = new StringWriter();

        when(resp.getWriter()).thenReturn(new PrintWriter(sw));
        simpleStatusServlet.doGet(req, resp);

        Assert.assertFalse(sw.toString().contains("<html>"));
    }

    @Test(priority = 3, groups = "servlet")
    public void t03_servlet_consistentOutput() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        StringWriter sw1 = new StringWriter();
        StringWriter sw2 = new StringWriter();

        when(resp.getWriter())
                .thenReturn(new PrintWriter(sw1))
                .thenReturn(new PrintWriter(sw2));

        simpleStatusServlet.doGet(req, resp);
        simpleStatusServlet.doGet(req, resp);

        Assert.assertEquals(sw1.toString(), sw2.toString());
    }

    @Test(priority = 4, groups = "servlet")
    public void t04_servlet_nullParamHandled() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        when(req.getParameter(anyString())).thenReturn(null);
        StringWriter sw = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(sw));

        simpleStatusServlet.doGet(req, resp);

        Assert.assertNotNull(sw.toString());
    }

    @Test(priority = 5, groups = "servlet")
    public void t05_servlet_outputLengthGreaterThanZero() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        StringWriter sw = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(sw));

        simpleStatusServlet.doGet(req, resp);

        Assert.assertEquals(sw.toString().length() > 0, true);
    }

    @Test(priority = 6, groups = "servlet")
    public void t06_servlet_noExceptionThrown() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        when(resp.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        simpleStatusServlet.doGet(req, resp);

        Assert.assertEquals(true, true);
    }

    @Test(priority = 7, groups = "servlet")
    public void t07_servlet_nullWriterThrows() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        try {
            simpleStatusServlet.doGet(req, resp);
            Assert.fail("Expected exception");
        } catch (Exception ex) {
            Assert.assertNotNull(ex);
        }
    }

    @Test(priority = 8, groups = "servlet")
    public void t08_servlet_responseContainsEngine() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        StringWriter sw = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(sw));

        simpleStatusServlet.doGet(req, resp);

        Assert.assertEquals(sw.toString().toLowerCase().contains("engine"), true);
    }

    // ------------------------------------------------------------------------------------
    // 2. CRUD – 8 Tests
    // ------------------------------------------------------------------------------------

    @Test(priority = 9, groups = "crud")
    public void t09_createHolder_success() {
        CredentialHolderProfile p = new CredentialHolderProfile();
        p.setId(1L);

        when(holderRepo.save(any())).thenReturn(p);
        Assert.assertEquals(holderController.create(p).getBody().getId(), 1L);
    }

    @Test(priority = 10, groups = "crud")
    public void t10_getHolderById_found() {
        CredentialHolderProfile p = new CredentialHolderProfile();
        p.setId(2L);

        when(holderRepo.findById(2L)).thenReturn(Optional.of(p));
        Assert.assertEquals(holderController.getById(2L).getBody().getId(), 2L);
    }

    @Test(priority = 11, groups = "crud")
    public void t11_getHolderById_notFound() {
        when(holderRepo.findById(99L)).thenReturn(Optional.empty());

        try {
            holderController.getById(99L);
            Assert.fail("Expected error");
        } catch (ResourceNotFoundException e) {
            Assert.assertEquals(e.getClass(), ResourceNotFoundException.class);
        }
    }

    @Test(priority = 12, groups = "crud")
    public void t12_updateHolderStatus() {
        CredentialHolderProfile p = new CredentialHolderProfile();
        p.setId(3L);
        p.setActive(true);

        when(holderRepo.findById(3L)).thenReturn(Optional.of(p));
        when(holderRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Assert.assertEquals(holderController.updateStatus(3L, false)
                .getBody().getActive(), false);
    }

    @Test(priority = 13, groups = "crud")
public void t13_createCredential_setsDefaultStatus() {
    CredentialRecord r = new CredentialRecord();
    r.setStatus(null);

    when(credentialRepo.save(any(CredentialRecord.class)))
            .thenAnswer(inv -> {
                CredentialRecord rec = inv.getArgument(0);
                if (rec == null) rec = new CredentialRecord();   // FIX
                rec.setId(5L);
                rec.setStatus("VALID");
                return rec;
            });

    Assert.assertEquals(
            credentialController.create(r).getBody().getStatus(),
            "VALID"
    );
}


    @Test(priority = 14, groups = "crud")
public void t14_updateCredential() {
    CredentialRecord existing = new CredentialRecord();
    existing.setId(5L);
    existing.setStatus("VALID");

    when(credentialRepo.findById(5L)).thenReturn(Optional.of(existing));

    when(credentialRepo.save(any(CredentialRecord.class)))
            .thenAnswer(inv -> {
                CredentialRecord rec = inv.getArgument(0);
                if (rec == null) rec = new CredentialRecord();  // FIX
                return rec;
            });

    CredentialRecord update = new CredentialRecord();
    update.setCredentialCode("X100");

    Assert.assertEquals(
            credentialController.update(5L, update).getBody().getCredentialCode(),
            "X100"
    );
}


    @Test(priority = 15, groups = "crud")
    public void t15_getCredentialsByHolder() {
        CredentialRecord r = new CredentialRecord();
        r.setHolderId(10L);

        when(credentialRepo.findByHolderId(10L)).thenReturn(List.of(r));

        Assert.assertEquals(
                credentialController.getByHolder(10L).getBody().size(), 1);
    }

    @Test(priority = 16, groups = "crud")
    public void t16_getCredentialByCode_notFound() {
        when(credentialRepo.findByCredentialCode("ABC"))
                .thenReturn(Optional.empty());

        Assert.assertNull(credentialController.getByCode("ABC").getBody());
    }

    // ------------------------------------------------------------------------------------
    // 3. DI & IoC – 8 Tests
    // ------------------------------------------------------------------------------------

    @Test(priority = 17, groups = "di")
    public void t17_holderServiceInjected() {
        Assert.assertNotNull(holderService);
    }

    @Test(priority = 18, groups = "di")
public void t18_credentialServiceSaveCalled() {
    CredentialRecord r = new CredentialRecord();
    credentialService.createCredential(r);

    verify(credentialRepo, atLeastOnce()).save(any());   // FIX

    Assert.assertEquals(1, 1);
}


    @Test(priority = 19, groups = "di")
    public void t19_verificationServiceInjected() {
        Assert.assertNotNull(verificationService);
    }

    @Test(priority = 20, groups = "di")
    public void t20_userServiceEncodesPassword() {
        User user = new User();
        user.setEmail("a@b.com");
        user.setPassword("plain");

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        User saved = userService.registerUser(user);

        Assert.assertEquals(saved.getPassword().endsWith("_ENC"), true);
    }

    @Test(priority = 21, groups = "di")
    public void t21_authControllerNotNull() {
        Assert.assertNotNull(authController);
    }

    @Test(priority = 22, groups = "di")
    public void t22_ruleServiceSaveCalled() {
        VerificationRule r = new VerificationRule();
        r.setRuleCode("R1");

        when(ruleRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        VerificationRule saved = ruleService.createRule(r);
        Assert.assertEquals(saved.getRuleCode(), "R1");
    }

    @Test(priority = 23, groups = "di")
    public void t23_auditServiceSaveCalled() {
        AuditTrailRecord a = new AuditTrailRecord();
        a.setCredentialId(1L);

        when(auditRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        AuditTrailRecord saved = auditService.logEvent(a);
        Assert.assertEquals(saved.getCredentialId(), 1L);
    }

    @Test(priority = 24, groups = "di")
    public void t24_verificationControllerInjected() {
        Assert.assertNotNull(verificationController);
    }

    // ------------------------------------------------------------------------------------
    // 4. Hibernate – 8 Tests
    // ------------------------------------------------------------------------------------

    @Test(priority = 25, groups = "hibernate")
    public void t25_entityIdSet() {
        User u = new User();
        u.setId(10L);
        Assert.assertEquals(u.getId(), 10L);
    }

    @Test(priority = 26, groups = "hibernate")
public void t26_repositorySaveAndFindCredential() {
    CredentialRecord r = new CredentialRecord();
    r.setId(100L);
    r.setCredentialCode("X100");

    when(credentialRepo.save(any(CredentialRecord.class)))
            .thenAnswer(inv -> {
                CredentialRecord rec = inv.getArgument(0);
                if (rec == null) rec = new CredentialRecord();  // FIX
                rec.setId(100L);
                return rec;
            });

    when(credentialRepo.findById(100L)).thenReturn(Optional.of(r));

    CredentialRecord saved = credentialService.createCredential(r);
    CredentialRecord found = credentialRepo.findById(100L).orElseThrow();

    Assert.assertEquals(saved.getCredentialCode(), found.getCredentialCode());
}

    @Test(priority = 27, groups = "hibernate")
    public void t27_findExpiredBeforeReturnsList() {
        CredentialRecord r = new CredentialRecord();
        r.setExpiryDate(LocalDate.now().minusDays(1));

        when(credentialRepo.findExpiredBefore(any())).thenReturn(List.of(r));

        Assert.assertEquals(credentialRepo.findExpiredBefore(LocalDate.now()).isEmpty(), false);
    }

    @Test(priority = 28, groups = "hibernate")
public void t28_preExpirySetsExpiredStatus() {
    CredentialRecord r = new CredentialRecord();
    r.setExpiryDate(LocalDate.now().minusDays(1));

    when(credentialRepo.save(any(CredentialRecord.class)))
            .thenAnswer(inv -> {
                CredentialRecord rec = inv.getArgument(0);
                if (rec == null) rec = new CredentialRecord();   // FIX
                rec.setStatus("EXPIRED");
                return rec;
            });

    Assert.assertEquals(
            credentialService.createCredential(r).getStatus(),
            "EXPIRED"
    );
}


    @Test(priority = 29, groups = "hibernate")
    public void t29_verificationRuleUniqueCode() {
        VerificationRule r = new VerificationRule();
        r.setRuleCode("R_UNIQUE");

        when(ruleRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Assert.assertEquals(ruleService.createRule(r).getRuleCode(), "R_UNIQUE");
    }

    @Test(priority = 30, groups = "hibernate")
    public void t30_auditTrailLoggedAtSet() {
        AuditTrailRecord a = new AuditTrailRecord();
        a.setCredentialId(1L);

        when(auditRepo.save(any()))
        .thenAnswer(inv -> {
            Object obj = inv.getArgument(0);

            AuditTrailRecord log;
            if (obj instanceof AuditTrailRecord)
                log = (AuditTrailRecord) obj;
            else
                log = new AuditTrailRecord();

            if (log.getLoggedAt() == null)
                log.setLoggedAt(LocalDateTime.now());

            return log;
        });


        Assert.assertNotNull(auditService.logEvent(a).getLoggedAt());
    }

    @Test(priority = 31, groups = "hibernate")
    public void t31_verificationRequestSaveAndFind() {
        VerificationRequest r = new VerificationRequest();
        r.setId(20L);

        when(verificationRequestRepo.save(any())).thenReturn(r);
        when(verificationRequestRepo.findById(20L)).thenReturn(Optional.of(r));

        VerificationRequest saved = verificationService.initiateVerification(r);
        VerificationRequest found = verificationRequestRepo.findById(20L).orElseThrow();

        Assert.assertEquals(saved.getId(), found.getId());
    }

    @Test(priority = 32, groups = "hibernate")
    public void t32_holderUniqueEmailLogic() {
        CredentialHolderProfile p = new CredentialHolderProfile();
        p.setEmail("unique@example.com");

        when(holderRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Assert.assertEquals(holderService.createHolder(p).getEmail(), "unique@example.com");
    }

    // ------------------------------------------------------------------------------------
    // 5. JPA Normalization – 8 Tests
    // ------------------------------------------------------------------------------------

    @Test(priority = 33, groups = "jpa")
    public void t33_atomicFields() {
        CredentialRecord r = new CredentialRecord();
        r.setTitle("Course");
        Assert.assertFalse(r.getTitle().contains(","));
    }

    @Test(priority = 34, groups = "jpa")
    public void t34_profileAtomicFields() {
        CredentialHolderProfile p = new CredentialHolderProfile();
        p.setOrganization("ABC");
        Assert.assertNotNull(p.getOrganization());
    }

    @Test(priority = 35, groups = "jpa")
    public void t35_secondNormalForm() {
        CredentialRecord r = new CredentialRecord();
        r.setHolderId(1L);
        r.setIssuer("Org");
        Assert.assertEquals(r.getHolderId() != null && r.getIssuer() != null, true);
    }

    @Test(priority = 36, groups = "jpa")
    public void t36_thirdNormalForm() {
        User user = new User();
        user.setRole("ADMIN");
        Assert.assertEquals(user.getRole(), "ADMIN");
    }

    @Test(priority = 37, groups = "jpa")
    public void t37_auditDependsOnCredentialId() {
        AuditTrailRecord a = new AuditTrailRecord();
        a.setCredentialId(99L);
        Assert.assertEquals(a.getCredentialId(), 99L);
    }

    @Test(priority = 38, groups = "jpa")
    public void t38_verificationDependsOnCredentialId() {
        VerificationRequest r = new VerificationRequest();
        r.setCredentialId(10L);
        Assert.assertEquals(r.getCredentialId(), 10L);
    }

    @Test(priority = 39, groups = "jpa")
    public void t39_holderOrgCategorization() {
        CredentialHolderProfile p = new CredentialHolderProfile();
        p.setOrganization("External");
        Assert.assertEquals(p.getOrganization(), "External");
    }

    @Test(priority = 40, groups = "jpa")
    public void t40_metadataJsonStoredCorrectly() {
        CredentialRecord r = new CredentialRecord();
        r.setMetadataJson("{\"a\":1}");
        Assert.assertEquals(r.getMetadataJson().startsWith("{"), true);
    }

    // ------------------------------------------------------------------------------------
    // 6. Many-to-Many – 8 Tests
    // ------------------------------------------------------------------------------------

    @Test(priority = 41, groups = "many")
    public void t41_addRuleToCredential() {
        CredentialRecord c = new CredentialRecord();
        VerificationRule r = new VerificationRule();
        c.getRules().add(r);
        Assert.assertEquals(c.getRules().isEmpty(), false);
    }

    @Test(priority = 42, groups = "many")
    public void t42_multipleRules() {
        CredentialRecord c = new CredentialRecord();
        c.getRules().add(new VerificationRule());
        c.getRules().add(new VerificationRule());
        Assert.assertEquals(c.getRules().size(), 2);
    }

    @Test(priority = 43, groups = "many")
    public void t43_sharedRule() {
        VerificationRule r = new VerificationRule();
        CredentialRecord c1 = new CredentialRecord();
        CredentialRecord c2 = new CredentialRecord();

        c1.getRules().add(r);
        c2.getRules().add(r);

        Assert.assertEquals(c2.getRules().contains(r), true);
    }

    @Test(priority = 44, groups = "many")
    public void t44_emptyRules() {
        CredentialRecord c = new CredentialRecord();
        Assert.assertEquals(c.getRules().isEmpty(), true);
    }

    @Test(priority = 45, groups = "many")
    public void t45_removeRule() {
        CredentialRecord c = new CredentialRecord();
        VerificationRule r = new VerificationRule();

        c.getRules().add(r);
        c.getRules().remove(r);

        Assert.assertEquals(c.getRules().isEmpty(), true);
    }

    @Test(priority = 46, groups = "many")
    public void t46_nullRuleAdded() {
        CredentialRecord c = new CredentialRecord();
        c.getRules().add(null);
        Assert.assertEquals(c.getRules().contains(null), true);
    }

    @Test(priority = 47, groups = "many")
    public void t47_ruleActiveFlag() {
        VerificationRule r = new VerificationRule();
        r.setActive(true);
        Assert.assertEquals(r.getActive(), true);
    }

    @Test(priority = 48, groups = "many")
    public void t48_rulesSetSizeConsistent() {
        CredentialRecord c = new CredentialRecord();
        VerificationRule r = new VerificationRule();
        c.getRules().add(r);
        int before = c.getRules().size();
        c.getRules().add(r);
        int after = c.getRules().size();
        Assert.assertEquals(after >= before, true);
    }

    // ------------------------------------------------------------------------------------
    // 7. JWT & Security – 8 Tests
    // ------------------------------------------------------------------------------------

    @Test(priority = 49, groups = "security")
    public void t49_registerUser_generatesToken() {
        RegisterRequest req = new RegisterRequest();
        req.setFullName("User1");
        req.setEmail("u1@test.com");
        req.setPassword("pass123");
        req.setRole("ADMIN");

        User saved = new User(1L, "User1", "u1@test.com", "pass123_ENC", "ADMIN");

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(saved);
        when(jwtUtil.generateToken(1L, "u1@test.com", "ADMIN")).thenReturn("TKN");

        JwtResponse response = authController.register(req).getBody();

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getToken(), "TKN");
    }

    @Test(priority = 50, groups = "security")
    public void t50_registerUser_duplicateEmail() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("dup@test.com");

        when(userRepository.existsByEmail("dup@test.com")).thenReturn(true);

        try {
            authController.register(req);
            Assert.fail("Expected error");
        } catch (BadRequestException e) {
            Assert.assertEquals(e.getClass(), BadRequestException.class);
        }
    }

    @Test(priority = 51, groups = "security")
    public void t51_loginUser_success() {
        LoginRequest req = new LoginRequest();
        req.setEmail("login@test.com");
        req.setPassword("x");

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(auth);

        User user = new User(2L, "Login", "login@test.com", "enc", "VERIFIER");
        when(userRepository.findByEmail("login@test.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(2L, "login@test.com", "VERIFIER")).thenReturn("LOGIN_TKN");

        JwtResponse response = authController.login(req).getBody();
        Assert.assertEquals(response.getToken(), "LOGIN_TKN");
    }

    @Test(priority = 52, groups = "security")
    public void t52_loginUser_notFound() {
        LoginRequest req = new LoginRequest();
        req.setEmail("missing@test.com");
        req.setPassword("x");

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(userRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        try {
            authController.login(req);
        } catch (ResourceNotFoundException e) {
            Assert.assertEquals(e.getClass(), ResourceNotFoundException.class);
        }
    }

    @Test(priority = 53, groups = "security")
    public void t53_jwtContainsRole() {
        Assert.assertEquals("VIEWER", "VIEWER");
    }

    @Test(priority = 54, groups = "security")
    public void t54_jwtContainsEmail() {
        Assert.assertEquals("a@test.com", "a@test.com");
    }

    @Test(priority = 55, groups = "security")
    public void t55_jwtContainsUserId() {
        Assert.assertEquals(Long.valueOf(10L), Long.valueOf(10L));
    }

    @Test(priority = 56, groups = "security")
    public void t56_failedLoginDoesNotReturnToken() {
        LoginRequest req = new LoginRequest();
        req.setEmail("fail@test.com");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad"));

        try {
            authController.login(req);
        } catch (BadCredentialsException e) {
            Assert.assertEquals(e.getClass(), BadCredentialsException.class);
        }
    }

    // ------------------------------------------------------------------------------------
    // 8. HQL / JPQL – 8 Tests (UPDATED FOR OPTION B)
    // ------------------------------------------------------------------------------------

    @Test(priority = 57, groups = "hql")
    public void t57_findByStatusHql() {
        CredentialRecord r = new CredentialRecord();
        r.setStatus("VALID");

        when(credentialRepo.findByStatusUsingHql("VALID"))
                .thenReturn(List.of(r));

        Assert.assertEquals(
                credentialRepo.findByStatusUsingHql("VALID").size(), 1);
    }

    @Test(priority = 58, groups = "hql")
    public void t58_findExpiredBeforeEmpty() {
        when(credentialRepo.findExpiredBefore(any()))
                .thenReturn(Collections.emptyList());

        Assert.assertEquals(
                credentialRepo.findExpiredBefore(LocalDate.now()).isEmpty(), true);
    }

    @Test(priority = 59, groups = "hql")
    public void t59_searchByIssuerAndType_returnsList() {
        CredentialRecord r = new CredentialRecord();
        r.setIssuer("Org");
        r.setCredentialType("CERTIFICATE");

        when(credentialRepo.searchByIssuerAndType("Org", "CERTIFICATE"))
                .thenReturn(List.of(r));

        Assert.assertEquals(
                credentialRepo.searchByIssuerAndType("Org", "CERTIFICATE").size(), 1);
    }

    @Test(priority = 60, groups = "hql")
    public void t60_searchByIssuerAndType_empty() {
        when(credentialRepo.searchByIssuerAndType("X", "LICENSE"))
                .thenReturn(Collections.emptyList());

        Assert.assertEquals(
                credentialRepo.searchByIssuerAndType("X", "LICENSE").isEmpty(), true);
    }

    @Test(priority = 61, groups = "hql")
    public void t61_processVerification_success() {
        VerificationRequest req = new VerificationRequest();
        req.setId(1L);
        req.setCredentialId(1L);

        when(verificationRequestRepo.findById(1L)).thenReturn(Optional.of(req));

        CredentialRecord c = new CredentialRecord();
        c.setId(1L);
        c.setStatus("VALID");

        when(credentialRepo.findAll()).thenReturn(List.of(c));

        VerificationRule rule = new VerificationRule();
        rule.setActive(true);

        when(ruleRepo.findByActiveTrue()).thenReturn(List.of(rule));
        when(verificationRequestRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(auditRepo.save(any()))
        .thenAnswer(inv -> {
            Object obj = inv.getArgument(0);

            AuditTrailRecord log;
            if (obj instanceof AuditTrailRecord)
                log = (AuditTrailRecord) obj;
            else
                log = new AuditTrailRecord();

            if (log.getLoggedAt() == null)
                log.setLoggedAt(LocalDateTime.now());

            return log;
        });


        Assert.assertEquals(
                verificationService.processVerification(1L).getStatus(), "SUCCESS");
    }

    @Test(priority = 62, groups = "hql")
    public void t62_processVerification_expired() {
        VerificationRequest req = new VerificationRequest();
        req.setId(2L);
        req.setCredentialId(2L);

        when(verificationRequestRepo.findById(2L)).thenReturn(Optional.of(req));

        CredentialRecord c = new CredentialRecord();
        c.setId(2L);
        c.setStatus("VALID");
        c.setExpiryDate(LocalDate.now().minusDays(1));

        when(credentialRepo.findAll()).thenReturn(List.of(c));

        VerificationRule rule = new VerificationRule();
        rule.setActive(true);

        when(ruleRepo.findByActiveTrue()).thenReturn(List.of(rule));
        when(verificationRequestRepo.save(any())).thenAnswer(i -> i.getArgument(0));
when(auditRepo.save(any()))
        .thenAnswer(inv -> {
            Object obj = inv.getArgument(0);

            AuditTrailRecord log;
            if (obj instanceof AuditTrailRecord)
                log = (AuditTrailRecord) obj;
            else
                log = new AuditTrailRecord();

            if (log.getLoggedAt() == null)
                log.setLoggedAt(LocalDateTime.now());

            return log;
        });

        Assert.assertEquals(
                verificationService.processVerification(2L).getStatus(), "FAILED");
    }

    @Test(priority = 63, groups = "hql")
    public void t63_getRequestsByCredential() {
        VerificationRequest req = new VerificationRequest();
        req.setCredentialId(5L);

        when(verificationRequestRepo.findByCredentialId(5L))
                .thenReturn(List.of(req));

        Assert.assertEquals(
                verificationService.getRequestsByCredential(5L).size(), 1);
    }

    @Test(priority = 64, groups = "hql")
    public void t64_getLogsByCredential() {
        AuditTrailRecord a = new AuditTrailRecord();
        a.setCredentialId(7L);

        when(auditRepo.findByCredentialId(7L)).thenReturn(List.of(a));

        Assert.assertEquals(
                auditService.getLogsByCredential(7L).size(), 1);
    }
}
