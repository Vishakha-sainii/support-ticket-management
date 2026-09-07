package com.supportticket.repository;

import com.supportticket.entity.Status;
import com.supportticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByStatus(Status status);

    @Query("""
            SELECT DISTINCT t FROM Ticket t
            LEFT JOIN FETCH t.comments
            WHERE t.id = :id
            """)
    Optional<Ticket> findByIdWithComments(@Param("id") Long id);

    @Query("""
            SELECT t FROM Ticket t
            WHERE (:status IS NULL OR t.status = :status)
            AND (
                :keyword IS NULL OR :keyword = ''
                OR LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            ORDER BY t.createdAt DESC
            """)
    List<Ticket> findByFilters(@Param("status") Status status, @Param("keyword") String keyword);
}
