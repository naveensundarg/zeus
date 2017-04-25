{

 :knowledge-base
 {

  :driving-1 (forall [?d ?t]
                     (if
                       (and (direction ?d) (holds (humans ?d 0) ?t))
                       (happens  (steer ?d) ?t)))

  :driving-2 (forall [?t]
                     (if (not (exists [?d]
                                      (and (direction ?d) (holds (humans ?d 0) ?t))))
                       (happens brake ?t)))



  :prempty (forall [?t]
                   (forall [?d ?car]
                           (if (and (direction ?d)
                                    (holds (in ?car ?d) ?t)
                                    (< 0 (ec ?car)))
                             (and (happens (aim-at ?car) ?t)
                                  (happens (accelerate-towards ?car) ?t)) )))


  :d0 (direction 0)
  :d1 (direction 1)


  :n1 (forall (x) (if (not (= 0 x))
                     (< 0 x)))
  :n2 (not (= 0 1))


  :humans (forall [?t ?d ?h1 ?h2]
                  (if  (and (holds (humans ?d ?h1) ?t)
             (holds (humans ?d ?h2) ?t))
                    (= ?h1 ?h2)))

  :all-directions (forall [?d]
                          (if (direction ?d)
                               (or (= ?d 0) (= ?d 1))))


  }
 }

