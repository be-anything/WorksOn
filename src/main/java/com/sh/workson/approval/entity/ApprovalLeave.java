package com.sh.workson.approval.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalLeave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seq_approval_form_id_generator")
    @SequenceGenerator(
            name = "seq_approval_form_id_generator",
            sequenceName = "seq_approval_form_id"
    )
    private Long id;
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, name = "start_date")
    private Timestamp startDate;

    @Column(nullable = false, name = "end_date")
    private Timestamp endDate;

    @Column(name = "leave_content")
    private String leaveContent;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @OneToMany(mappedBy = "approvalLeave", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Approval> approvals = new ArrayList<>();



}
