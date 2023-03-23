import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TrackFieldEntryDetailComponent } from './track-field-entry-detail.component';

describe('TrackFieldEntry Management Detail Component', () => {
  let comp: TrackFieldEntryDetailComponent;
  let fixture: ComponentFixture<TrackFieldEntryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TrackFieldEntryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ trackFieldEntry: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TrackFieldEntryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TrackFieldEntryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load trackFieldEntry on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.trackFieldEntry).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
