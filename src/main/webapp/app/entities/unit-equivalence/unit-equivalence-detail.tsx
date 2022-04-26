import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './unit-equivalence.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UnitEquivalenceDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const unitEquivalenceEntity = useAppSelector(state => state.unitEquivalence.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="unitEquivalenceDetailsHeading">
          <Translate contentKey="managerProductStockApp.unitEquivalence.detail.title">UnitEquivalence</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{unitEquivalenceEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="managerProductStockApp.unitEquivalence.name">Name</Translate>
            </span>
          </dt>
          <dd>{unitEquivalenceEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="managerProductStockApp.unitEquivalence.description">Description</Translate>
            </span>
          </dt>
          <dd>{unitEquivalenceEntity.description}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.unitEquivalence.unit">Unit</Translate>
          </dt>
          <dd>{unitEquivalenceEntity.unit ? unitEquivalenceEntity.unit.name : ''}</dd>
          <dt>
            <Translate contentKey="managerProductStockApp.unitEquivalence.child">Child</Translate>
          </dt>
          <dd>{unitEquivalenceEntity.child ? unitEquivalenceEntity.child.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/unit-equivalence" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/unit-equivalence/${unitEquivalenceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UnitEquivalenceDetail;
