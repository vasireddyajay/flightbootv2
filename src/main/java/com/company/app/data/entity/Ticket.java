package com.company.app.data.entity;

import javax.persistence.*;

@Entity
@Table(name="TICKET")
public class Ticket {
    @Id
    @Column(name="TICKET_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GUEST_ID", nullable = false)
    private Guest guest;

    @Column(name="ITERNARY")
    private String iternaryString;

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public String getIternaryString() {
        return iternaryString;
    }

    public void setIternaryString(String iternaryString) {
        this.iternaryString = iternaryString;
    }
}
