import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TrackFieldEntryAthleteDetailsDetailComponent } from './track-field-entry-athlete-details-detail.component';

describe('TrackFieldEntryAthleteDetails Management Detail Component', () => {
  let comp: TrackFieldEntryAthleteDetailsDetailComponent;
  let fixture: ComponentFixture<TrackFieldEntryAthleteDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TrackFieldEntryAthleteDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ trackFieldEntryAthleteDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TrackFieldEntryAthleteDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TrackFieldEntryAthleteDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load trackFieldEntryAthleteDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.trackFieldEntryAthleteDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
